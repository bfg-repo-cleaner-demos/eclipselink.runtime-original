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

/**
 * The query BNF for a numeric literal.
 *
 * @version 11.2.0
 * @since 11.2.0
 * @author Pascal Filion
 */
@SuppressWarnings("nls")
final class NumericLiteralBNF extends JPQLQueryBNF
{
	/**
	 * The unique identifier of this BNF rule.
	 */
	static final String ID = "numeric_literal";

	/**
	 * Creates a new <code>NumericLiteralExpressionBNF</code>.
	 */
	NumericLiteralBNF()
	{
		super(ID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void initialize()
	{
		registerExpressionFactory(LiteralExpressionFactory.ID);
	}
}