/*******************************************************************************
 * Copyright (c) 2006, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.persistence.jpa.internal.jpql.parser;

/**
 * This {@link IdentificationVariableFactory} creates a new {@link IdentificationVariable}.
 *
 * @see IdentificationVariable
 *
 * @version 2.3
 * @since 2.3
 * @author Pascal Filion
 */
@SuppressWarnings("nls")
final class IdentificationVariableFactory extends ExpressionFactory {

	/**
	 * The unique identifier of this {@link IdentificationVariableFactory}.
	 */
	static final String ID = "identification-variable";

	/**
	 * Creates a new <code>IdentificationVariableFactory</code>.
	 */
	IdentificationVariableFactory() {
		super(ID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	AbstractExpression buildExpression(AbstractExpression parent,
	                                   WordParser wordParser,
	                                   String word,
	                                   JPQLQueryBNF queryBNF,
	                                   AbstractExpression expression,
	                                   boolean tolerant) {

		if (word.length() > 0) {
			expression = new IdentificationVariable(parent, word);
			expression.parse(wordParser, tolerant);
			return expression;
		}

		return null;
	}
}