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
package org.eclipse.persistence.jpa.tests.jpql.parser;

import org.junit.Test;
import static org.eclipse.persistence.jpa.tests.jpql.parser.JPQLParserTester.*;

@SuppressWarnings("nls")
public final class ResultVariableTest extends JPQLParserTest {

	@Test
	public void test_JPQLQuery_01() {

		String jpqlQuery = "SELECT e AS n FROM Employee e";

		ExpressionTester selectStatement = selectStatement(
			select(resultVariableAs(variable("e"), "n")),
			from("Employee", "e")
		);

		testQuery(jpqlQuery, selectStatement);
	}

	@Test
	public void test_JPQLQuery_02() {

		String jpqlQuery = "SELECT e n FROM Employee e";

		ExpressionTester selectStatement = selectStatement(
			select(resultVariable(variable("e"), "n")),
			from("Employee", "e")
		);

		testQuery(jpqlQuery, selectStatement);
	}

	@Test
	public void test_JPQLQuery_03() {

		String jpqlQuery = "SELECT AVG(e.age) AS g FROM Employee e";

		ExpressionTester selectStatement = selectStatement(
			select(resultVariableAs(avg("e.age"), "g")),
			from("Employee", "e")
		);

		testQuery(jpqlQuery, selectStatement);
	}

	@Test
	public void test_JPQLQuery_04() {

		String jpqlQuery = "SELECT AVG(e.age) g FROM Employee e";

		ExpressionTester selectStatement = selectStatement(
			select(resultVariable(avg("e.age"), "g")),
			from("Employee", "e")
		);

		testQuery(jpqlQuery, selectStatement);
	}

	@Test
	public void test_JPQLQuery_05() {

		String jpqlQuery = "SELECT AVG(e.age) + 2 AS g FROM Employee e";

		ExpressionTester selectStatement = selectStatement(
			select(resultVariableAs(avg("e.age").add(numeric(2)), "g")),
			from("Employee", "e")
		);

		testQuery(jpqlQuery, selectStatement);
	}

	@Test
	public void test_JPQLQuery_06() {

		String jpqlQuery = "SELECT AVG(e.age) + 2 AS g FROM Employee e";

		ExpressionTester selectStatement = selectStatement(
			select(resultVariableAs(avg("e.age").add(numeric(2)), "g")),
			from("Employee", "e")
		);

		testQuery(jpqlQuery, selectStatement);
	}

	@Test
	public void test_JPQLQuery_07() {

		String jpqlQuery = "SELECT AVG(e.age) AS g, e.name AS n FROM Employee e";

		ExpressionTester selectStatement = selectStatement(
			select(
				resultVariableAs(avg("e.age"), "g"),
				resultVariableAs(path("e.name"), "n")
			),
			from("Employee", "e")
		);

		testQuery(jpqlQuery, selectStatement);
	}

	@Test
	public void test_JPQLQuery_08() {

		String jpqlQuery = "SELECT AVG(e.age) g, e.name n FROM Employee e";

		ExpressionTester selectStatement = selectStatement(
			select(
				resultVariable(avg("e.age"), "g"),
				resultVariable(path("e.name"), "n")
			),
			from("Employee", "e")
		);

		testQuery(jpqlQuery, selectStatement);
	}

	@Test
	public void test_JPQLQuery_09() {

		String jpqlQuery = "SELECT AVG(e.age) AS";

		ResultVariableTester resultVariable = resultVariableAs(avg(path("e.age")), nullExpression());
		resultVariable.hasSpaceAfterAs = false;

		ExpressionTester selectStatement = selectStatement(
			select(resultVariable)
		);

		testInvalidQuery(jpqlQuery, selectStatement);
	}

	@Test
	public void test_JPQLQuery_10() {

		String jpqlQuery = "SELECT AVG(e.age) AS ";

		ExpressionTester selectStatement = selectStatement(
			select(resultVariableAs(avg(path("e.age")), nullExpression()))
		);

		testInvalidQuery(jpqlQuery, selectStatement);
	}

	@Test
	public void test_JPQLQuery_11() {

		String jpqlQuery = "SELECT AS";

		ResultVariableTester resultVariable = resultVariableAs(nullExpression(), nullExpression());
		resultVariable.hasSpaceAfterAs = false;

		ExpressionTester selectStatement = selectStatement(
			select(resultVariable)
		);

		testInvalidQuery(jpqlQuery, selectStatement);
	}

	@Test
	public void test_JPQLQuery_12() {

		String jpqlQuery = "SELECT AS ";

		ExpressionTester selectStatement = selectStatement(
			select(resultVariableAs(nullExpression(), nullExpression()))
		);

		testInvalidQuery(jpqlQuery, selectStatement);
	}

	@Test
	public void test_JPQLQuery_13() {

		String jpqlQuery = "SELECT AS n";

		ExpressionTester selectStatement = selectStatement(
			select(resultVariableAs(nullExpression(), "n"))
		);

		testInvalidQuery(jpqlQuery, selectStatement);
	}

	@Test
	public void test_JPQLQuery_14() {

		String jpqlQuery = "SELECT e AS emp FROM Employee e ORDER BY emp";

		SelectStatementTester selectStatement = selectStatement(
			select(resultVariableAs(variable("e"), "emp")),
			from("Employee", "e"),
			orderBy(orderByItem(variable("emp")))
		);

		testQuery(jpqlQuery, selectStatement);
	}

	@Test
	public void test_JPQLQuery_15() {

		String jpqlQuery = "SELECT e.name, AVG(e.age) AS age FROM Employee e ORDER BY age";

		SelectStatementTester selectStatement = selectStatement(
			select(path("e.name"), resultVariableAs(avg("e.age"), "age")),
			from("Employee", "e"),
			orderBy(orderByItem(variable("age")))
		);

		testQuery(jpqlQuery, selectStatement);
	}
}