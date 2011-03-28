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
 * One of the aggregate functions. The arguments must be numeric. <b>AVG</b> returns <code>Double</code>.
 * <p>
 * <div nowrap><b>BNF:</b> <code>expression ::= AVG([DISTINCT] state_field_path_expression)</code><p>
 *
 * @version 2.3
 * @since 2.3
 * @author Pascal Filion
 */
public final class AvgFunction extends AggregateFunction {

	/**
	 * Creates a new <code>AvgFunction</code>.
	 *
	 * @param parent The parent of this expression
	 */
	AvgFunction(AbstractExpression parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	public void accept(ExpressionVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	String parseIdentifier(WordParser wordParser) {
		return AVG;
	}
}