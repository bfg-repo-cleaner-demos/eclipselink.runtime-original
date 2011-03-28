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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @version 2.3
 * @since 2.3
 * @author Pascal Filion
 */
public final class JPQLQueryBNFAccessor {

	private static Iterator<String> aggregates(Iterator<String> identifiers) {
		return filter(identifiers, IdentifierRole.AGGREGATE);
	}

	private static Iterator<String> clauses(Iterator<String> identifiers) {
		return filter(identifiers, IdentifierRole.CLAUSE);
	}

	public static Iterator<String> collectionMemberDeclarationParameters() {
		return identifiers(CollectionValuedPathExpressionBNF.ID);
	}

	public static Iterator<String> comparisonExpressionClauses() {
		return clauses(comparisonExpressionIdentifiers());
	}

	public static Iterator<String> comparisonExpressionFunctions() {
		return functions(comparisonExpressionIdentifiers());
	}

	public static Iterator<String> comparisonExpressionIdentifiers() {
		return identifiers(ComparisonExpressionBNF.ID);
	}

	public static Iterator<String> constructorItemFunctions() {
		return functions(constructorItemIdentifiers());
	}

	public static Iterator<String> constructorItemIdentifiers() {
		return identifiers(ConstructorItemBNF.ID);
	}

	public static Iterator<String> countFunctions() {
		return functions(countIdentifiers());
	}

	private static Iterator<String> countIdentifiers() {
		return identifiers(InternalCountBNF.ID);
	}

	private static Iterator<String> filter(Iterator<String> identifiers,
	                                       IdentifierRole identifierRole) {

		List<String> items = new ArrayList<String>();

		while (identifiers.hasNext()) {
			String identifier = identifiers.next();

			if (identifierRole(identifier) == identifierRole) {
				items.add(identifier);
			}
		}

		return items.iterator();
	}

	private static Iterator<String> functions(Iterator<String> identifiers) {
		return filter(identifiers, IdentifierRole.FUNCTION);
	}

	public static IdentifierRole identifierRole(String identifier) {
		return AbstractExpression.identifierRole(identifier);
	}

	public static Iterator<String> identifiers(String queryBNFId) {
		return queryBNF(queryBNFId).identifiers();
	}

	private static JPQLQueryBNF queryBNF(String queryBNFId) {
		return AbstractExpression.queryBNF(queryBNFId);
	}

	public static Iterator<String> scalarExpressionFunctions() {
		return functions(scalarExpressionIdentifiers());
	}

	public static Iterator<String> scalarExpressionIdentifiers() {
		return identifiers(ScalarExpressionBNF.ID);
	}

	public static Iterator<String> selectItemAggregates() {
		return aggregates(selectItemIdentifiers());
	}

	public static Iterator<String> selectItemFunctions() {
		return functions(selectItemIdentifiers());
	}

	public static Iterator<String> selectItemIdentifiers() {
		return identifiers(SelectItemBNF.ID);
	}

	public static Iterator<String> subSelectFunctions() {
		return functions(subSelectIdentifiers());
	}

	public static Iterator<String> subSelectIdentifiers() {
		return identifiers(SimpleSelectExpressionBNF.ID);
	}

	public static Iterator<String> whereClauseFunctions() {
		return functions(whereClauseIdentifiers());
	}

	public static Iterator<String> whereClauseIdentifiers() {
		return identifiers(ConditionalExpressionBNF.ID);
	}
}