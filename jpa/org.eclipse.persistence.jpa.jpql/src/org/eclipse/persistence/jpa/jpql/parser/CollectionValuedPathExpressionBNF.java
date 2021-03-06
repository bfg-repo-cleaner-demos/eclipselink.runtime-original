/*******************************************************************************
 * Copyright (c) 2006, 2013 Oracle and/or its affiliates. All rights reserved.
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
package org.eclipse.persistence.jpa.jpql.parser;

/**
 * The query BNF for a collection-valued path expression.
 * <p>
 * JPA 1.0:
 * <div nowrap><b>BNF:</b> <code>collection_valued_path_expression ::= identification_variable.{single_valued_object_field.}*collection_valued_field</code><p>
 *
 * JPA 2.0:
 * <div nowrap><b>BNF:</b> <code>collection_valued_path_expression ::= general_identification_variable.{single_valued_object_field.}*collection_valued_field</code><p>
 *
 * @version 2.5
 * @since 2.3
 * @author Pascal Filion
 */
@SuppressWarnings("nls")
public final class CollectionValuedPathExpressionBNF extends JPQLQueryBNF {

	/**
	 * The unique identifier of this BNF rule.
	 */
	public static final String ID = "collection_valued_path_expression";

	/**
	 * Creates a new <code>CollectionValuedPathExpressionBNF</code>.
	 */
	public CollectionValuedPathExpressionBNF() {
		super(ID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialize() {
		super.initialize();
		setFallbackBNFId(ID);
		setFallbackExpressionFactoryId(CollectionValuedPathExpressionFactory.ID);
	}
}