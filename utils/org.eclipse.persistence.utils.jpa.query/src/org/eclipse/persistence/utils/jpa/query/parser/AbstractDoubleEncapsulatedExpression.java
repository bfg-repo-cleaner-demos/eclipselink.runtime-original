/*******************************************************************************
 * Copyright (c) 2006, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available athttp://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle
 *
 ******************************************************************************/
package org.eclipse.persistence.utils.jpa.query.parser;

import java.util.Collection;
import java.util.List;

/**
 * This {@link Expression} takes care of parsing an expression that encapsulates
 * two expressions separated by a comma.
 *
 * <div nowrap><b>BNF:</b> <code>expression ::= &lt;identifier&gt;(first_expression, second_expression)</code><p>
 *
 * @see ConcatExpression
 * @see ModExpression
 * @see NullIfExpression
 *
 * @version 11.2.0
 * @since 11.2.0
 * @author Pascal Filion
 */
public abstract class AbstractDoubleEncapsulatedExpression extends AbstractEncapsulatedExpression
{
	/**
	 * The {@link Expression} that represents the first expression.
	 */
	private AbstractExpression firstExpression;

	/**
	 * Determines whether the comma separating the first and second expression
	 * was parsed.
	 */
	private boolean hasComma;

	/**
	 * Determines whether a whitespace is following the comma.
	 */
	private boolean hasSpaceAfterComma;

	/**
	 * The {@link Expression} that represents the second expression.
	 */
	private AbstractExpression secondExpression;

	/**
	 * Creates a new <code>AbstractDoubleEncapsulatedExpression</code>.
	 *
	 * @param parent The parent of this expression
	 */
	AbstractDoubleEncapsulatedExpression(AbstractExpression parent)
	{
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void addChildrenTo(Collection<Expression> children)
	{
		children.add(getFirstExpression());
		children.add(getSecondExpression());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void addOrderedEncapsulatedExpressionTo(List<StringExpression> children)
	{
		// Fist expression
		if (firstExpression != null)
		{
			children.add(firstExpression);
		}

		// ','
		if (hasComma)
		{
			children.add(buildStringExpression(COMMA));
		}

		if (hasSpaceAfterComma)
		{
			children.add(buildStringExpression(SPACE));
		}

		// Second expression
		if (secondExpression != null)
		{
			children.add(secondExpression);
		}
	}

	/**
	 * Returns the {@link Expression} that represents the first expression.
	 *
	 * @return The expression that was parsed representing the first expression
	 */
	public final Expression getFirstExpression()
	{
		if (firstExpression == null)
		{
			firstExpression = buildNullExpression();
		}

		return firstExpression;
	}

	/**
	 * Returns the {@link Expression} that represents the second expression.
	 *
	 * @return The expression that was parsed representing the second expression
	 */
	public final Expression getSecondExpression()
	{
		if (secondExpression == null)
		{
			secondExpression = buildNullExpression();
		}

		return secondExpression;
	}

	/**
	 * Determines whether the comma was parsed after the first expression.
	 *
	 * @return <code>true</code> if a comma was parsed after the first expression;
	 * <code>false</code> otherwise
	 */
	public final boolean hasComma()
	{
		return hasComma;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean hasEncapsulatedExpression()
	{
		return hasFirstExpression() || hasComma || hasSecondExpression();
	}

	/**
	 * Determines whether the first expression of the query was parsed.
	 *
	 * @return <code>true</code> if the first expression was parsed;
	 * <code>false</code> if it was not parsed
	 */
	public final boolean hasFirstExpression()
	{
		return firstExpression != null &&
		      !firstExpression.isNull();
	}

	/**
	 * Determines whether the second expression of the query was parsed.
	 *
	 * @return <code>true</code> if the second expression was parsed;
	 * <code>false</code> if it was not parsed
	 */
	public final boolean hasSecondExpression()
	{
		return secondExpression != null &&
		      !secondExpression.isNull();
	}

	/**
	 * Determines whether a whitespace was parsed after the comma.
	 *
	 * @return <code>true</code> if there was a whitespace after the comma;
	 * <code>false</code> otherwise
	 */
	public final boolean hasSpaceAfterComma()
	{
		return hasSpaceAfterComma;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean isParsingComplete(WordParser wordParser, String word)
	{
		char character = wordParser.character();

		return character == COMMA             ||
		       character == RIGHT_PARENTHESIS ||
		       super.isParsingComplete(wordParser, word);
	}

	/**
	 * Returns the BNF to be used to parse one of the encapsulated expression.
	 *
	 * @param index The position of the encapsulated {@link Expression} that
	 * needs to be parsed within the parenthesis
	 * @return The BNF to be used to parse one of the encapsulated expression
	 */
	abstract JPQLQueryBNF parameterExpressionBNF(int index);

	/**
	 * {@inheritDoc}
	 */
	@Override
	void parseEncapsulatedExpression(WordParser wordParser, boolean tolerant)
	{
		int count = 0;

		// Parse the first expression
		firstExpression = parse
		(
			wordParser,
			parameterExpressionBNF(1),
			tolerant
		);

		if (hasFirstExpression())
		{
			count = wordParser.skipLeadingWhitespace();
		}

		// Parse ','
		hasComma = wordParser.startsWith(COMMA);

		if (hasComma)
		{
			count = 0;
			wordParser.moveForward(1);
			hasSpaceAfterComma = wordParser.skipLeadingWhitespace() > 0;
		}
		else if (hasFirstExpression())
		{
			hasSpaceAfterComma = (count > 0);
			count = 0;
		}

		// Parse the second expression
		secondExpression = parse
		(
			wordParser,
			parameterExpressionBNF(2),
			tolerant
		);

		if (!hasSecondExpression())
		{
			wordParser.moveBackward(count);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void toParsedTextEncapsulatedExpression(StringBuilder writer)
	{
		// First expression
		if (firstExpression != null)
		{
			firstExpression.toParsedText(writer);
		}

		// ','
		if (hasComma)
		{
			writer.append(COMMA);
		}

		if (hasSpaceAfterComma)
		{
			writer.append(SPACE);
		}

		// Second expression
		if (secondExpression != null)
		{
			secondExpression.toParsedText(writer);
		}
	}
}