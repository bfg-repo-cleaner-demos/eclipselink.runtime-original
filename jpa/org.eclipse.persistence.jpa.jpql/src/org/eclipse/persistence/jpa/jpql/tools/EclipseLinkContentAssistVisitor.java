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
package org.eclipse.persistence.jpa.jpql.tools;

import java.util.List;
import org.eclipse.persistence.jpa.jpql.EclipseLinkVersion;
import org.eclipse.persistence.jpa.jpql.ExpressionTools;
import org.eclipse.persistence.jpa.jpql.LiteralType;
import org.eclipse.persistence.jpa.jpql.parser.AbstractEclipseLinkExpressionVisitor;
import org.eclipse.persistence.jpa.jpql.parser.AbstractFromClause;
import org.eclipse.persistence.jpa.jpql.parser.AbstractPathExpression;
import org.eclipse.persistence.jpa.jpql.parser.AbstractSelectStatement;
import org.eclipse.persistence.jpa.jpql.parser.AsOfClause;
import org.eclipse.persistence.jpa.jpql.parser.CastExpression;
import org.eclipse.persistence.jpa.jpql.parser.CollectionExpression;
import org.eclipse.persistence.jpa.jpql.parser.CollectionValuedPathExpressionBNF;
import org.eclipse.persistence.jpa.jpql.parser.ConnectByClause;
import org.eclipse.persistence.jpa.jpql.parser.DatabaseType;
import org.eclipse.persistence.jpa.jpql.parser.EclipseLinkExpressionVisitor;
import org.eclipse.persistence.jpa.jpql.parser.Expression;
import org.eclipse.persistence.jpa.jpql.parser.ExtractExpression;
import org.eclipse.persistence.jpa.jpql.parser.FromClause;
import org.eclipse.persistence.jpa.jpql.parser.HierarchicalQueryClause;
import org.eclipse.persistence.jpa.jpql.parser.OrderByItem;
import org.eclipse.persistence.jpa.jpql.parser.OrderByItem.Ordering;
import org.eclipse.persistence.jpa.jpql.parser.OrderSiblingsByClause;
import org.eclipse.persistence.jpa.jpql.parser.PatternValueBNF;
import org.eclipse.persistence.jpa.jpql.parser.RegexpExpression;
import org.eclipse.persistence.jpa.jpql.parser.ScalarExpressionBNF;
import org.eclipse.persistence.jpa.jpql.parser.SelectStatement;
import org.eclipse.persistence.jpa.jpql.parser.SimpleFromClause;
import org.eclipse.persistence.jpa.jpql.parser.SimpleSelectStatement;
import org.eclipse.persistence.jpa.jpql.parser.StartWithClause;
import org.eclipse.persistence.jpa.jpql.parser.SubqueryBNF;
import org.eclipse.persistence.jpa.jpql.parser.TableExpression;
import org.eclipse.persistence.jpa.jpql.parser.TableVariableDeclaration;
import org.eclipse.persistence.jpa.jpql.parser.UnionClause;
import org.eclipse.persistence.jpa.jpql.tools.ContentAssistProposals.ClassType;
import org.eclipse.persistence.jpa.jpql.tools.resolver.Declaration;
import static org.eclipse.persistence.jpa.jpql.parser.AbstractExpression.*;
import static org.eclipse.persistence.jpa.jpql.parser.Expression.*;

/**
 * This extension over the default content assist visitor adds the additional support EclipseLink
 * provides.
 * <p>
 * Provisional API: This interface is part of an interim API that is still under development and
 * expected to change significantly before reaching stability. It is available at this early stage
 * to solicit feedback from pioneering adopters on the understanding that any code that uses this
 * API will almost certainly be broken (repeatedly) as the API evolves.
 *
 * @version 2.5
 * @since 2.4
 * @author Pascal Filion
 */
@SuppressWarnings("unused") // unused used for the import statement: see bug 330740
public class EclipseLinkContentAssistVisitor extends AbstractContentAssistVisitor
                                             implements EclipseLinkExpressionVisitor {

	/**
	 * Creates a new <code>EclipseLinkContentAssistVisitor</code>.
	 *
	 * @param queryContext The context used to query information about the query
	 * @exception NullPointerException The {@link JPQLQueryContext} cannot be <code>null</code>
	 */
	public EclipseLinkContentAssistVisitor(JPQLQueryContext queryContext) {
		super(queryContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AcceptableTypeVisitor buildAcceptableTypeVisitor() {
		return new AcceptableTypeVisitor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AppendableExpressionVisitor buildAppendableExpressionVisitor() {
		return new AppendableExpressionVisitor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EndingQueryPositionBuilder buildEndingQueryPositionBuilder() {
		return new EndingQueryPositionBuilder();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected FollowingClausesVisitor buildFollowingClausesVisitor() {
		return new FollowingClausesVisitor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected FromClauseCollectionHelper buildFromClauseCollectionHelper() {
		return new FromClauseCollectionHelper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected FromClauseStatementHelper buildFromClauseStatementHelper() {
		return new FromClauseStatementHelper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected GroupByClauseCollectionHelper buildGroupByClauseCollectionHelper() {
		return new GroupByClauseCollectionHelper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IncompleteCollectionExpressionVisitor buildIncompleteCollectionExpressionVisitor() {
		return new IncompleteCollectionExpressionVisitor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected OrderByClauseStatementHelper buildOrderByClauseStatementHelper() {
		return new OrderByClauseStatementHelper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected SimpleFromClauseStatementHelper buildSimpleFromClauseStatementHelper() {
		return new SimpleFromClauseStatementHelper();
	}

	protected TableExpressionVisitor buildTableExpressionVisitor() {
		return new TableExpressionVisitor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected TrailingCompletenessVisitor buildTrailingCompletenessVisitor() {
		return new TrailingCompletenessVisitor();
	}

	protected UnionClauseStatementHelper buildUnionClauseStatementHelper() {
		return new UnionClauseStatementHelper();
	}

	/**
	 * Returns the enum constant of the EclipseLink version specified in the {@link JPQLQueryContext}.
	 *
	 * @return The EclipseLink version specified or the default version (i.e. the version of the
	 * current release)
	 * @since 2.5
	 */
	protected EclipseLinkVersion getEcliseLinkVersion() {
		return EclipseLinkVersion.value(queryContext.getProviderVersion());
	}

	protected TableExpressionVisitor getTableExpressionVisitor() {
		TableExpressionVisitor visitor = getHelper(TableExpressionVisitor.class);
		if (visitor == null) {
			visitor = buildTableExpressionVisitor();
			registerHelper(TableExpressionVisitor.class, visitor);
		}
		return visitor;
	}

	protected String getTableName(String variableName) {

		Declaration declaration = queryContext.getDeclaration(variableName);
		Expression baseExpression = (declaration != null) ? declaration.getBaseExpression() : null;

		if ((baseExpression != null) && isTableExpression(baseExpression)) {
	   	return queryContext.literal(baseExpression, LiteralType.STRING_LITERAL);
		}

		return null;
	}

	protected UnionClauseStatementHelper getUnionClauseStatementHelper() {
		UnionClauseStatementHelper helper = getHelper(UnionClauseStatementHelper.class);
		if (helper == null) {
			helper = buildUnionClauseStatementHelper();
			registerHelper(UnionClauseStatementHelper.class, helper);
		}
		return helper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialize() {
		super.initialize();
		compoundTypeFilters.put(REGEXP, CompoundTypeFilter.VALID_INSTANCE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isJoinFetchIdentifiable() {
		EclipseLinkVersion version = EclipseLinkVersion.value(queryContext.getProviderVersion());
		return version.isNewerThanOrEqual(EclipseLinkVersion.VERSION_2_4);
	}

	protected boolean isTableExpression(Expression expression) {
		TableExpressionVisitor visitor = getTableExpressionVisitor();
		try {
			visitor.expression = expression;
			expression.accept(visitor);
			return visitor.valid;
		}
		finally {
			visitor.valid = false;
			visitor.expression = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void visit(AsOfClause expression) {
		super.visit(expression);
		int position = queryPosition.getPosition(expression) - corrections.peek();

		// Within "AS OF"
		if (isPositionWithin(position, AS_OF)) {
			proposals.addIdentifier(AS_OF);
		}
		// After "AS OF"
		else if (expression.hasSpaceAfterIdentifier()) {

			int length = AS_OF.length() + SPACE_LENGTH;

			// Right after "AS OF "
			if (position == length) {
				addIdentifier(SCN);
				addIdentifier(TIMESTAMP);

				if (!expression.hasScn() &&
				    !expression.hasTimestamp()) {

					addIdentificationVariables();
					addFunctionIdentifiers(ScalarExpressionBNF.ID);
				}
			}
			// After "AS OF SCN" or "AS OF TIMESTAMP"
			else if (expression.hasScn() ||
			         expression.hasSpaceAfterIdentifier()) {

				// SCN
				if (expression.hasScn() && isPositionWithin(position, length, SCN)) {
					proposals.addIdentifier(SCN);
					proposals.addIdentifier(TIMESTAMP);
				}
				// TIMESTAMP
				else if (expression.hasTimestamp() && isPositionWithin(position, length, TIMESTAMP)) {
					proposals.addIdentifier(SCN);
					proposals.addIdentifier(TIMESTAMP);
				}
				else {

					if (expression.hasScn()) {
						length += SCN.length();
					}
					else if (expression.hasTimestamp()) {
						length += TIMESTAMP.length();
					}

					// After "AS OF SCN " or "AS OF TIMESTAMP "
					if (expression.hasSpaceAfterCategory()) {
						addIdentificationVariables();
						addFunctionIdentifiers(ScalarExpressionBNF.ID);
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void visit(CastExpression expression) {
		super.visit(expression);
		int position = queryPosition.getPosition(expression) - corrections.peek();
		String identifier = expression.getIdentifier();

		// Within CAST
		if (isPositionWithin(position, identifier)) {
			addIdentifier(identifier);
			addIdentificationVariables();
			addFunctionIdentifiers(expression.getParent().findQueryBNF(expression));
		}
		// After "CAST("
		else if (expression.hasLeftParenthesis()) {
			int length = identifier.length() + 1 /* '(' */;

			// Right after "CAST("
			if (position == length) {
				addIdentificationVariables();
				addFunctionIdentifiers(expression.getEncapsulatedExpressionQueryBNFId());
			}
			else if (expression.hasExpression()) {
				Expression scalarExpression = expression.getExpression();

				if (isComplete(scalarExpression)) {
					length += scalarExpression.getLength();

					if (expression.hasSpaceAfterExpression()) {
						length++;

						// Right before "AS" or database type
						if (position == length) {
							addAggregateIdentifiers(expression.getEncapsulatedExpressionQueryBNFId());
							proposals.addIdentifier(AS);
						}
						// Within "AS"
						else if (isPositionWithin(position, length, AS)) {
							proposals.addIdentifier(AS);
						}
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void visit(ConnectByClause expression) {
		super.visit(expression);
		int position = queryPosition.getPosition(expression) - corrections.peek();

		// Within "CONNECT BY"
		if (isPositionWithin(position, CONNECT_BY)) {
			proposals.addIdentifier(CONNECT_BY);
		}
		// After "CONNECT BY"
		else if (expression.hasSpaceAfterConnectBy()) {

			int length = CONNECT_BY.length() + SPACE_LENGTH;

			// Right after "CONNECT BY "
			if (position == length) {
				addIdentificationVariables();
				addFunctionIdentifiers(CollectionValuedPathExpressionBNF.ID);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void visit(DatabaseType expression) {
		super.visit(expression);
		// Nothing to do, this is database specific
	}

	/**
	 * {@inheritDoc}
	 */
	public void visit(ExtractExpression expression) {
		super.visit(expression);
		int position = queryPosition.getPosition(expression) - corrections.peek();
		String identifier = expression.getIdentifier();

		// Within "EXTRACT"
		if (isPositionWithin(position, identifier)) {
			proposals.addIdentifier(identifier);
			addFunctionIdentifiers(expression);
		}
		// After "EXTRACT("
		else if (expression.hasLeftParenthesis()) {
			int length = identifier.length() + 1 /* '(' */;

			// Right after "EXTRACT("
			if (position == length) {
				// Nothing to do, unless we show basic date parts
			}

			if (expression.hasDatePart()) {
				String datePart = expression.getDatePart();

				// Within "<date part>"
				if (isPositionWithin(position, length, datePart)) {
					// Nothing to do, unless we show basic date parts
				}

				length += datePart.length();

				// After "<date part> "
				if (expression.hasSpaceAfterDatePart()) {
					length++;

					// Right before "FROM"
					if (position == length) {
						addIdentifier(FROM);

						// Only add the scalar expression's functions if it is not specified
						// or the FROM identifier is not present
						if (!expression.hasExpression() || !expression.hasFrom()) {
							addIdentificationVariables();
							addFunctionIdentifiers(expression.getEncapsulatedExpressionQueryBNFId());
						}
					}
				}
			}

			if (expression.hasFrom()) {

				// Within "FROM"
				if (isPositionWithin(position, length, FROM)) {
					proposals.addIdentifier(FROM);

					// Only add the scalar expression's functions if it is not specified
					if (!expression.hasExpression()) {
						addIdentificationVariables();
						addFunctionIdentifiers(expression.getEncapsulatedExpressionQueryBNFId());
					}
				}

				length += 4 /* FROM */;

				if (expression.hasSpaceAfterFrom()) {
					length++;
				}

				// Right after "FROM "
				if (position == length) {
					addIdentificationVariables();
					addFunctionIdentifiers(expression.getEncapsulatedExpressionQueryBNFId());
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void visit(HierarchicalQueryClause expression) {
		super.visit(expression);
		int position = queryPosition.getPosition(expression) - corrections.peek();

		// At the beginning of the clause
		if (position == 0) {
			addIdentifier(START_WITH);

			if (!expression.hasStartWithClause()) {
				addIdentifier(CONNECT_BY);
			}
		}
		else {
			int length = 0;

			// After the start with clause
			if (expression.hasStartWithClause()) {
				length += expression.getStartWithClause().getLength();

				// Right after the start with clause
				if (hasVirtualSpace() && (position == length + SPACE_LENGTH)) {
					addIdentifier(CONNECT_BY);
				}
				// After the start with clause
				else if (expression.hasSpaceAfterStartWithClause()) {
					length++;

					// Right after the start with clause
					if (position == length) {
						addIdentifier(CONNECT_BY);
					}
				}
			}

			length += expression.getConnectByClause().getLength();

			// Right after the connect by clause
			if (hasVirtualSpace() && (position == length + SPACE_LENGTH)) {
				addIdentifier(ORDER_SIBLINGS_BY);
			}
			// After the connect by clause
			else if (expression.hasSpaceAfterConnectByClause()) {
				length++;

				if (position == length) {
					addIdentifier(ORDER_SIBLINGS_BY);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(OrderByItem expression) {
		super.visit(expression);
		int position = queryPosition.getPosition(expression) - corrections.peek();

		// After the order by item
		if (expression.hasExpression()) {
			int length = expression.getExpression().getLength();

			if (expression.hasSpaceAfterExpression()) {
				length++;

				// Right after the order by item
				if (position == length) {

					// Only add "NULLS FIRST" and "NULLS LAST" if the ordering is not specified
					if (expression.getOrdering() == Ordering.DEFAULT) {
						proposals.addIdentifier(NULLS_FIRST);
						proposals.addIdentifier(NULLS_LAST);
					}
				}
				else {
					length += expression.getActualOrdering().length();

					if (position > length) {
						if (expression.hasSpaceAfterOrdering()) {
							length += SPACE_LENGTH;

							// Right before "NULLS FIRST" or "NULLS LAST"
							if (position == length) {
								proposals.addIdentifier(NULLS_FIRST);
								proposals.addIdentifier(NULLS_LAST);
							}
							else {
								String nullOrdering = expression.getActualNullOrdering();

								// Within "NULLS FIRST" or "NULLS LAST"
								if (isPositionWithin(position, length, nullOrdering)) {
									proposals.addIdentifier(NULLS_FIRST);
									proposals.addIdentifier(NULLS_LAST);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void visit(OrderSiblingsByClause expression) {
		if (!isLocked(expression)) {
			super.visit(expression);
			visitCollectionExpression(expression, ORDER_SIBLINGS_BY, getOrderByClauseCollectionHelper());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void visit(RegexpExpression expression) {
		super.visit(expression);
		int position = queryPosition.getPosition(expression) - corrections.peek();
		int length = 0;

		if (expression.hasStringExpression()) {
			length += expression.getStringExpression().getLength();

			if (expression.hasSpaceAfterStringExpression()) {
				length += SPACE_LENGTH;
			}
		}

		// Within "REGEXP"
		if (isPositionWithin(position, length, REGEXP)) {
			proposals.addIdentifier(REGEXP);
		}
		// After "REGEXP"
		else {
			length += 6 /* REGEXP */;

			// After "REGEXP "
			if (expression.hasSpaceAfterIdentifier()) {
				length += SPACE_LENGTH;

				// Right after "REGEXP "
				addIdentificationVariables();
				addFunctionIdentifiers(PatternValueBNF.ID);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void visit(StartWithClause expression) {
		if (!isLocked(expression)) {
			super.visit(expression);
			visitCollectionExpression(expression, expression.getIdentifier(), getAbstractConditionalClauseCollectionHelper());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void visit(TableExpression expression) {
		super.visit(expression);
		int position = queryPosition.getPosition(expression);

		// Within "TABLE"
		if (isPositionWithin(position, TABLE)) {
			proposals.addIdentifier(TABLE);
		}
		// After '('
		else if (expression.hasLeftParenthesis()) {
			int length = TABLE.length() + SPACE_LENGTH;

			// Right after '('
			if (position == length) {
				proposals.setTableNamePrefix(ExpressionTools.EMPTY_STRING);
			}
			else {
				Expression nameExpression = expression.getExpression();
				String tableName = queryContext.literal(nameExpression, LiteralType.STRING_LITERAL);

				if (tableName.length() == 0) {
					tableName = queryContext.literal(nameExpression, LiteralType.IDENTIFICATION_VARIABLE);
				}

				int tableNameLength = tableName.length();

				// Within the string literal representing the table name
				if ((position > length) && (position <= length + tableNameLength)) {
					String prefix = tableName.substring(0, position - length);
					prefix = ExpressionTools.unquote(prefix);
					proposals.setTableNamePrefix(prefix);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void visit(TableVariableDeclaration expression) {
		super.visit(expression);

		TableExpression tableExpression = expression.getTableExpression();
		int position = queryPosition.getPosition(expression) - corrections.peek();
		int length = tableExpression.getLength();

		// After "TABLE()"
		if (expression.hasSpaceAfterTableExpression()) {
			length += SPACE_LENGTH;

			// Right after "TABLE() "
			if (isPositionWithin(position, length, AS)) {
				addIdentifier(AS);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void visit(UnionClause expression) {
		super.visit(expression);
		int position = queryPosition.getPosition(expression) - corrections.peek();
		String identifier = expression.getIdentifier();

		// Within <identifier>
		if (isPositionWithin(position, identifier)) {
			proposals.addIdentifier(EXCEPT);
			proposals.addIdentifier(INTERSECT);
			proposals.addIdentifier(UNION);
		}
		// After "<identifier> "
		else if (expression.hasSpaceAfterIdentifier()) {
			int length = identifier.length() + SPACE_LENGTH;

			// Right after "<identifier> "
			if (position == length) {
				proposals.addIdentifier(ALL);

				if (!expression.hasAll()) {
					addIdentifier(SELECT);
				}
			}
			// Within "ALL"
			else if (isPositionWithin(position, length, ALL)) {
				addIdentifier(ALL);
			}
			else {
				if ((position == length) && !expression.hasAll()) {
					proposals.addIdentifier(SELECT);
				}
				else {

					if (expression.hasAll()) {
						length += 3 /* ALL */;
					}

					// After "ALL "
					if (expression.hasSpaceAfterAll()) {
						length += SPACE_LENGTH;

						// Right after "ALL "
						if (position == length) {
							proposals.addIdentifier(SELECT);
						}
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void visitThirdPartyPathExpression(AbstractPathExpression expression,
	                                             String variableName) {

		// Check to see if a column name can be resolved
		int position = queryPosition.getPosition(expression);
		String text = expression.toActualText();
		int dotIndex = text.indexOf(DOT);
		int secondDotIndex = (dotIndex > -1) ? text.indexOf(DOT, dotIndex + 1) : -1;

		// The cursor position is after the first dot and either there is no second dot or the
		// position is before the second dot, which means a table name and column names could
		// potentially be resolved
		if ((secondDotIndex == -1) || (position < secondDotIndex)) {
			String tableName = getTableName(variableName);

			if (tableName != ExpressionTools.EMPTY_STRING) {
				tableName = ExpressionTools.unquote(tableName);
				proposals.setTableName(tableName, text.substring(dotIndex + 1, position));
			}
		}
	}

	protected class AcceptableTypeVisitor extends AbstractContentAssistVisitor.AcceptableTypeVisitor {
	}

	protected class AppendableExpressionVisitor extends AbstractContentAssistVisitor.AppendableExpressionVisitor
	                                            implements EclipseLinkExpressionVisitor {

		/**
		 * {@inheritDoc}
		 */
		public void visit(AsOfClause expression) {
			if (expression.hasExpression()) {
				expression.getExpression().accept(this);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(CastExpression expression) {
			if (expression.hasExpression()) {
				expression.getExpression().accept(this);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(ConnectByClause expression) {
			if (expression.hasExpression()) {
				expression.getExpression().accept(this);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(DatabaseType expression) {
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(ExtractExpression expression) {
			if (expression.hasExpression()) {
				expression.getExpression().accept(this);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void visit(FromClause expression) {

			if (expression.hasAsOfClause()) {
				expression.getAsOfClause().accept(this);
			}
			else if (expression.hasHierarchicalQueryClause()) {
				expression.getHierarchicalQueryClause().accept(this);
			}
			else {
				super.visit(expression);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(HierarchicalQueryClause expression) {

			if (expression.hasOrderSiblingsByClause()) {
				expression.getOrderSiblingsByClause().accept(this);
			}
			else if (expression.hasConnectByClause()) {
				expression.getConnectByClause().accept(this);
			}
			else if (expression.hasStartWithClause()) {
				expression.getStartWithClause().accept(this);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(OrderSiblingsByClause expression) {
			clauseOfItems = true;
			super.visit(expression);
			clauseOfItems = false;
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(RegexpExpression expression) {
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void visit(SimpleFromClause expression) {

			if (expression.hasAsOfClause()) {
				expression.getAsOfClause().accept(this);
			}
			else if (expression.hasHierarchicalQueryClause()) {
				expression.getHierarchicalQueryClause().accept(this);
			}
			else {
				super.visit(expression);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(StartWithClause expression) {
			if (expression.hasConditionalExpression()) {
				expression.getConditionalExpression().accept(this);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(TableExpression expression) {
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(TableVariableDeclaration expression) {
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(UnionClause expression) {
			if (expression.hasQuery()) {
				expression.getQuery().accept(this);
			}
		}
	}

	protected class EndingQueryPositionBuilder extends AbstractContentAssistVisitor.EndingQueryPositionBuilder
	                                           implements EclipseLinkExpressionVisitor {

		/**
		 * {@inheritDoc}
		 */
		public void visit(AsOfClause expression) {

			if (badExpression) {
				return;
			}

			if (expression.hasExpression()) {
				expression.getExpression().accept(this);
			}

			if (queryPosition.getExpression() == null) {
				queryPosition.setExpression(expression);
			}

			queryPosition.addPosition(expression, expression.getLength() - correction);
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(CastExpression expression) {

			if (badExpression) {
				return;
			}

			if (expression.hasScalarExpression() &&
			   !expression.hasAs() &&
			   !expression.hasDatabaseType() &&
			   !expression.hasRightParenthesis()) {

				expression.getExpression().accept(this);
			}

			if (queryPosition.getExpression() == null) {
				queryPosition.setExpression(expression);
			}

			queryPosition.addPosition(expression, expression.getLength() - correction);
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(ConnectByClause expression) {

			if (badExpression) {
				return;
			}

			if (expression.hasExpression()) {
				expression.getExpression().accept(this);
			}

			if (queryPosition.getExpression() == null) {
				queryPosition.setExpression(expression);
			}

			queryPosition.addPosition(expression, expression.getLength() - correction);
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(DatabaseType expression) {
			visitAbstractDoubleEncapsulatedExpression(expression);
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(ExtractExpression expression) {
			visitAbstractSingleEncapsulatedExpression(expression);
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(HierarchicalQueryClause expression) {

			if (badExpression) {
				return;
			}

			if (expression.hasOrderSiblingsByClause()) {
				expression.getOrderSiblingsByClause().accept(this);
			}
			else if (expression.hasConnectByClause()) {
				expression.getConnectByClause().accept(this);
				if (expression.hasSpaceAfterConnectByClause()) {
					virtualSpace = true;
				}
			}
			else if (expression.hasStartWithClause()) {
				expression.getStartWithClause().accept(this);
				if (expression.hasSpaceAfterStartWithClause()) {
					virtualSpace = true;
				}
			}

			if (queryPosition.getExpression() == null) {
				queryPosition.setExpression(expression);
			}

			queryPosition.addPosition(expression, expression.getLength() - correction);
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(OrderSiblingsByClause expression) {

			if (badExpression) {
				return;
			}

			if (expression.hasOrderByItems()) {
				expression.getOrderByItems().accept(this);
			}

			if (queryPosition.getExpression() == null) {
				queryPosition.setExpression(expression);
			}

			queryPosition.addPosition(expression, expression.getLength() - correction);
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(RegexpExpression expression) {

			if (badExpression) {
				return;
			}

			if (expression.hasPatternValue()) {
				expression.getPatternValue().accept(this);
			}

			if (queryPosition.getExpression() == null) {
				queryPosition.setExpression(expression);
			}

			queryPosition.addPosition(expression, expression.getLength() - correction);
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(StartWithClause expression) {
			visitAbstractConditionalClause(expression);
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(TableExpression expression) {
			visitAbstractSingleEncapsulatedExpression(expression);
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(TableVariableDeclaration expression) {

			if (badExpression) {
				return;
			}

			if (expression.hasIdentificationVariable()) {
				expression.getIdentificationVariable().accept(this);
			}
			else if (!expression.hasAs()) {
				expression.getTableExpression().accept(this);
			}

			if (queryPosition.getExpression() == null) {
				queryPosition.setExpression(expression);
			}

			queryPosition.addPosition(expression, expression.getLength() - correction);
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(UnionClause expression) {

			if (badExpression) {
				return;
			}

			if (expression.hasQuery()) {
				expression.getQuery().accept(this);
			}

			if (queryPosition.getExpression() == null) {
				queryPosition.setExpression(expression);
			}

			queryPosition.addPosition(expression, expression.getLength() - correction);
		}
	}

	/**
	 * This visitor adds support for the additional clauses provided by EclipseLink, such as the
	 */
	protected class FollowingClausesVisitor extends AbstractContentAssistVisitor.FollowingClausesVisitor {

		protected boolean hasAsOfClause;
		protected boolean hasConnectByClause;
		protected boolean hasOrderSiblingsByClause;
		protected boolean hasStartWithClause;
		protected boolean introspect;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void dispose() {
			super.dispose();
			hasAsOfClause            = false;
			hasConnectByClause       = false;
			hasStartWithClause       = false;
			hasOrderSiblingsByClause = false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected boolean hasFromClause(AbstractSelectStatement expression) {

			introspect = true;
			expression.getFromClause().accept(this);
			introspect = false;

			if (afterIdentifier == SELECT) {

				if (beforeIdentifier == START_WITH) {
					return expression.hasFromClause();
				}

				if (beforeIdentifier == CONNECT_BY) {
					return expression.hasFromClause() ||
					       hasStartWithClause;
				}

				if (beforeIdentifier == ORDER_SIBLINGS_BY) {
					return expression.hasFromClause() ||
					       hasStartWithClause         ||
					       hasConnectByClause;
				}

				if (beforeIdentifier == AS_OF) {
					return expression.hasFromClause() ||
					       hasStartWithClause         ||
					       hasConnectByClause         ||
					       hasOrderSiblingsByClause;
				}

				if (beforeIdentifier == WHERE) {
					return expression.hasFromClause() ||
					       hasStartWithClause         ||
					       hasConnectByClause         ||
					       hasOrderSiblingsByClause   ||
					       hasAsOfClause;
				}
			}
			else if (afterIdentifier == FROM) {

				if (beforeIdentifier == CONNECT_BY) {
					return hasStartWithClause;
				}

				if (beforeIdentifier == ORDER_SIBLINGS_BY) {
					return hasStartWithClause ||
					       hasConnectByClause;
				}

				if (beforeIdentifier == AS_OF) {
					return hasStartWithClause ||
					       hasConnectByClause ||
					       hasOrderSiblingsByClause;
				}

				if (beforeIdentifier == WHERE) {
					return hasStartWithClause       ||
					       hasConnectByClause       ||
					       hasOrderSiblingsByClause ||
					       hasAsOfClause;
				}
			}
			else if (afterIdentifier == START_WITH) {

				if (beforeIdentifier == ORDER_SIBLINGS_BY) {
					return hasConnectByClause;
				}

				if (beforeIdentifier == AS_OF) {
					return hasConnectByClause ||
					       hasOrderSiblingsByClause;
				}

				if (beforeIdentifier == WHERE) {
					return hasConnectByClause       ||
					       hasOrderSiblingsByClause ||
					       hasAsOfClause;
				}
			}
			else if (afterIdentifier == CONNECT_BY) {

				if (beforeIdentifier == AS_OF) {
					return hasOrderSiblingsByClause;
				}

				if (beforeIdentifier == WHERE) {
					return hasOrderSiblingsByClause ||
					       hasAsOfClause;
				}
			}
			else if (afterIdentifier == ORDER_SIBLINGS_BY) {

				if (beforeIdentifier == WHERE) {
					return hasAsOfClause;
				}
			}

			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void visit(FromClause expression) {

			if (!introspect) {
				super.visit(expression);
			}
			else {
				hasAsOfClause = expression.hasAsOfClause();

				if (expression.hasHierarchicalQueryClause()) {
					expression.getHierarchicalQueryClause().accept(this);
				}
			}
		}

		public void visit(HierarchicalQueryClause expression) {

			if (!introspect) {
				super.visit(expression);
			}
			else {
				hasConnectByClause       = expression.hasConnectByClause();
				hasStartWithClause       = expression.hasStartWithClause();
				hasOrderSiblingsByClause = expression.hasOrderSiblingsByClause();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void visit(SimpleFromClause expression) {

			if (!introspect) {
				super.visit(expression);
			}
			else {
				hasAsOfClause = expression.hasAsOfClause();

				if (expression.hasHierarchicalQueryClause()) {
					expression.getHierarchicalQueryClause().accept(this);
				}
			}
		}
	}

	protected class FromClauseCollectionHelper extends AbstractContentAssistVisitor.FromClauseCollectionHelper {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addAtTheEndOfChild(AbstractFromClause expression,
		                               CollectionExpression collectionExpression,
		                               int index,
		                               boolean hasComma,
		                               boolean virtualSpace) {

 			super.addAtTheEndOfChild(expression, collectionExpression, index, hasComma, virtualSpace);
 			boolean end = (index + 1 == collectionExpression.childrenSize());

			// At the end of a range variable declaration, the following clauses can be added
			// Example: "SELECT e FROM Employee e |"
			// Example: "SELECT e FROM Employee e, Address a |"
 			// Example: "SELECT e FROM Employee e |, Address a " <- Not valid to add the clauses
			if (((index == 0) || hasComma) && end && virtualSpace) {

				EclipseLinkContentAssistVisitor.this.addIdentifier(START_WITH);

				if (!hasClausesDefinedBetween(expression, FROM, CONNECT_BY)) {
					EclipseLinkContentAssistVisitor.this.addIdentifier(CONNECT_BY);
				}

				if (!hasClausesDefinedBetween(expression, FROM, ORDER_SIBLINGS_BY)) {
					EclipseLinkContentAssistVisitor.this.addIdentifier(CONNECT_BY);
				}

				if (!hasClausesDefinedBetween(expression, FROM, AS_OF)) {
					EclipseLinkContentAssistVisitor.this.addIdentifier(AS_OF);
				}
			}
			// Special case to handle a range variable declaration that can also
			// be either the beginning of the following clauses
			// Example: "SELECT e FROM Employee o o|" <- Valid
			// Example: "SELECT e FROM Employee o, Address a o|" <- Valid
			// Example: "SELECT e FROM Employee o|" <- Not valid
			else if ((index > 0) && end && !hasComma) {

				int position = queryPosition.getPosition();
				addCompositeIdentifier(START_WITH, 4 /* START - 1 */);

				if (!hasClausesDefinedBetween(expression, FROM, CONNECT_BY)) {
					addCompositeIdentifier(CONNECT_BY, 6 /* CONNECT - 1 */);
				}

				if (!hasClausesDefinedBetween(expression, FROM, ORDER_SIBLINGS_BY)) {
					addCompositeIdentifier(ORDER_SIBLINGS_BY, 4 /* ORDER - 1 */);
				}

				// AS OF clause
				if (!hasClausesDefinedBetween(expression, FROM, AS_OF)) {
					addCompositeIdentifier(AS_OF, 1 /* AS - 1 */);
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addTheBeginningOfChild(AbstractFromClause expression,
		                                   CollectionExpression collectionExpression,
		                                   int index,
		                                   boolean hasComma) {

			super.addTheBeginningOfChild(expression, collectionExpression, index, hasComma);

			if (hasComma) {
				proposals.setClassNamePrefix(word, ClassType.INSTANTIABLE);
			}

			if (collectionExpression != null) {
				boolean end = (index + 1 == collectionExpression.childrenSize());

	 			if (end && !hasComma) {

					EclipseLinkContentAssistVisitor.this.addIdentifier(START_WITH);

					if (!hasClausesDefinedBetween(expression, FROM, CONNECT_BY)) {
						EclipseLinkContentAssistVisitor.this.addIdentifier(CONNECT_BY);
					}

					if (!hasClausesDefinedBetween(expression, FROM, ORDER_SIBLINGS_BY)) {
						EclipseLinkContentAssistVisitor.this.addIdentifier(CONNECT_BY);
					}

					if (!hasClausesDefinedBetween(expression, FROM, AS_OF)) {
						EclipseLinkContentAssistVisitor.this.addIdentifier(AS_OF);
					}
	 			}
			}
		}
	}

	protected class FromClauseStatementHelper extends AbstractContentAssistVisitor.FromClauseStatementHelper {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addInternalClauseProposals(SelectStatement expression) {
			super.addInternalClauseProposals(expression);

			if (!hasClausesDefinedBetween(expression, FROM, WHERE)) {
				EclipseLinkContentAssistVisitor.this.addIdentifier(START_WITH);
			}

			if (!hasClausesDefinedBetween(expression, START_WITH, ORDER_SIBLINGS_BY)) {
				EclipseLinkContentAssistVisitor.this.addIdentifier(CONNECT_BY);
			}

			if (!hasClausesDefinedBetween(expression, CONNECT_BY, AS_OF)) {
				EclipseLinkContentAssistVisitor.this.addIdentifier(ORDER_SIBLINGS_BY);
			}

			if (!hasClausesDefinedBetween(expression, ORDER_SIBLINGS_BY, WHERE)) {
				EclipseLinkContentAssistVisitor.this.addIdentifier(AS_OF);
			}
		}
	}

	/**
	 * This subclass adds support for EclipseLink specific support.
	 */
	protected class IncompleteCollectionExpressionVisitor extends AbstractContentAssistVisitor.IncompleteCollectionExpressionVisitor {

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected List<String> compositeIdentifiersAfter(String clause) {

			// Add support for hierarchical query and AS OF clauses
			if ((clause == FROM) && getEcliseLinkVersion().isNewerThanOrEqual(EclipseLinkVersion.VERSION_2_5)) {
				List<String> identifiers = super.compositeIdentifiersAfter(clause);
				identifiers.add(START_WITH);
				identifiers.add(CONNECT_BY);
				identifiers.add(ORDER_SIBLINGS_BY);
				identifiers.add(AS_OF);
				return identifiers;
			}

			return super.compositeIdentifiersAfter(clause);
		}
	}

	protected class OrderByClauseStatementHelper extends AbstractContentAssistVisitor.OrderByClauseStatementHelper {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public UnionClauseStatementHelper getNextHelper() {
			return getUnionClauseStatementHelper();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasSpaceAfterClause(SelectStatement expression) {
			return expression.hasSpaceBeforeUnion();
		}
	}

	protected class SimpleFromClauseStatementHelper extends AbstractContentAssistVisitor.SimpleFromClauseStatementHelper {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addInternalClauseProposals(SimpleSelectStatement expression) {
			super.addInternalClauseProposals(expression);

			EclipseLinkContentAssistVisitor.this.addIdentifier(START_WITH);

			if (!hasClausesDefinedBetween(expression, FROM, CONNECT_BY)) {
				EclipseLinkContentAssistVisitor.this.addIdentifier(CONNECT_BY);
			}

			if (!hasClausesDefinedBetween(expression, FROM, ORDER_SIBLINGS_BY)) {
				EclipseLinkContentAssistVisitor.this.addIdentifier(CONNECT_BY);
			}

			if (!hasClausesDefinedBetween(expression, FROM, AS_OF)) {
				EclipseLinkContentAssistVisitor.this.addIdentifier(AS_OF);
			}
		}
	}

	protected class TableExpressionVisitor extends AbstractEclipseLinkExpressionVisitor {

		/**
		 * The {@link Expression} being visited.
		 */
		protected Expression expression;

		/**
		 * <code>true</code> if the {@link Expression} being visited is a {@link TableExpression}.
		 */
		protected boolean valid;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void visit(TableExpression expression) {
			valid = (this.expression == expression);
		}
	}

	protected class TrailingCompletenessVisitor extends AbstractContentAssistVisitor.TrailingCompletenessVisitor
	                                            implements EclipseLinkExpressionVisitor {

		/**
		 * {@inheritDoc}
		 */
		public void visit(AsOfClause expression) {

			complete = expression.hasExpression();

			if (complete) {
				expression.getExpression().accept(this);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(CastExpression expression) {
			complete = expression.hasRightParenthesis();
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(ConnectByClause expression) {
			complete = expression.hasExpression();
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(DatabaseType expression) {
			// Always complete since it's a single word
		}


		/**
		 * {@inheritDoc}
		 */
		public void visit(ExtractExpression expression) {
			complete = expression.hasRightParenthesis();
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(HierarchicalQueryClause expression) {

			if (expression.hasOrderSiblingsByClause()) {
				expression.getOrderSiblingsByClause().accept(this);
			}
			else {
				complete = expression.hasConnectByClause();

				if (complete) {
					expression.getConnectByClause().accept(this);
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(OrderSiblingsByClause expression) {

			complete = expression.hasOrderByItems();

			if (complete) {
				expression.getOrderByItems().accept(this);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(RegexpExpression expression) {
			complete = expression.hasPatternValue();
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(StartWithClause expression) {

			complete = expression.hasConditionalExpression();

			if (complete) {
				expression.getConditionalExpression().accept(this);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(TableExpression expression) {
			complete = expression.hasRightParenthesis();
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(TableVariableDeclaration expression) {
			complete = expression.hasIdentificationVariable();
		}

		/**
		 * {@inheritDoc}
		 */
		public void visit(UnionClause expression) {

			complete = expression.hasQuery();

			if (complete) {
				expression.getQuery().accept(this);
			}
		}
	}

	protected class UnionClauseStatementHelper implements StatementHelper<SelectStatement> {

		/**
		 * {@inheritDoc}
		 */
		public void addClauseProposals() {
			addIdentifier(EXCEPT);
			addIdentifier(INTERSECT);
			addIdentifier(UNION);
		}

		/**
		 * {@inheritDoc}
		 */
		public void addInternalClauseProposals(SelectStatement expression) {
		}

		/**
		 * {@inheritDoc}
		 */
		public Expression getClause(SelectStatement expression) {
			return expression.getUnionClauses();
		}

		/**
		 * {@inheritDoc}
		 */
		public StatementHelper<? extends SelectStatement> getNextHelper() {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean hasClause(SelectStatement expression) {
			return expression.hasUnionClauses();
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean hasSpaceAfterClause(SelectStatement expression) {
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isClauseComplete(SelectStatement expression) {

			UnionClause unionClause = (UnionClause) expression.getUnionClauses();
			Expression subquery = unionClause.getQuery();
			boolean complete = isValid(subquery, SubqueryBNF.ID);

			if (complete) {
				complete = isComplete(subquery);
			}

			return complete;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isRequired() {
			return false;
		}
	}
}