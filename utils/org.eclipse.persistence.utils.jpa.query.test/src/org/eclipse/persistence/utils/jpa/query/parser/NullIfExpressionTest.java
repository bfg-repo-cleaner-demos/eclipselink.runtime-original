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

import org.eclipse.persistence.utils.jpa.query.parser.JPQLTests.QueryStringFormatter;
import org.junit.Test;

@SuppressWarnings("nls")
public final class NullIfExpressionTest extends AbstractJPQLTest
{
	private QueryStringFormatter buildFormatter_9()
	{
		return new QueryStringFormatter()
		{
			@Override
			public String format(String query)
			{
				return query.replace(",)", ", )");
			}
		};
	}

	@Override
	boolean isTolerant()
	{
		return true;
	}

	@Test
	public void testBuildExpression_1()
	{
		String query = "SELECT NULLIF('JPQL', 4 + e.age) FROM Employee e";

		ExpressionTester selectStatement = selectStatement
		(
			select(nullIf(string("'JPQL'"), numeric(4).add(path("e.age")))),
			from("Employee", "e")
		);

		testQuery(query, selectStatement);
	}

	@Test
	public void testBuildExpression_2()
	{
		String query = "SELECT NULLIF() FROM Employee e";

		NullIfExpressionTester nullIf = nullIf(nullExpression(), nullExpression());
		nullIf.hasComma = false;
		nullIf.hasSpaceAfterComma = false;

		ExpressionTester selectStatement = selectStatement
		(
			select(nullIf),
			from("Employee", "e")
		);

		testQuery(query, selectStatement);
	}

	@Test
	public void testBuildExpression_3()
	{
		String query = "SELECT NULLIF(,) FROM Employee e";

		NullIfExpressionTester nullIf = nullIf(nullExpression(), nullExpression());
		nullIf.hasComma = true;
		nullIf.hasSpaceAfterComma = false;

		ExpressionTester selectStatement = selectStatement
		(
			select(nullIf),
			from("Employee", "e")
		);

		testQuery(query, selectStatement);
	}

	@Test
	public void testBuildExpression_4()
	{
		String query = "SELECT NULLIF FROM Employee e";

		NullIfExpressionTester nullIf = nullIf(nullExpression(), nullExpression());
		nullIf.hasLeftParenthesis = false;
		nullIf.hasComma = false;
		nullIf.hasSpaceAfterComma = false;
		nullIf.hasRightParenthesis = false;

		ExpressionTester selectStatement = selectStatement
		(
			select(nullIf),
			from("Employee", "e")
		);

		testQuery(query, selectStatement);
	}

	@Test
	public void testBuildExpression_5()
	{
		String query = "SELECT NULLIF( FROM Employee e";

		NullIfExpressionTester nullIf = nullIf(nullExpression(), nullExpression());
		nullIf.hasLeftParenthesis = true;
		nullIf.hasComma = false;
		nullIf.hasSpaceAfterComma = false;
		nullIf.hasRightParenthesis = false;

		ExpressionTester selectStatement = selectStatement
		(
			select(nullIf),
			from("Employee", "e")
		);

		testQuery(query, selectStatement);
	}

	@Test
	public void testBuildExpression_6()
	{
		String query = "SELECT NULLIF) FROM Employee e";

		NullIfExpressionTester nullIf = nullIf(nullExpression(), nullExpression());
		nullIf.hasLeftParenthesis = false;
		nullIf.hasComma = false;
		nullIf.hasSpaceAfterComma = false;
		nullIf.hasRightParenthesis = true;

		ExpressionTester selectStatement = selectStatement
		(
			select(nullIf),
			from("Employee", "e")
		);

		testQuery(query, selectStatement);
	}

	@Test
	public void testBuildExpression_7()
	{
		String query = "SELECT NULLIF(e.name) FROM Employee e";

		NullIfExpressionTester nullIf = nullIf(path("e.name"), nullExpression());
		nullIf.hasComma = false;
		nullIf.hasSpaceAfterComma = false;

		ExpressionTester selectStatement = selectStatement
		(
			select(nullIf),
			from("Employee", "e")
		);

		testQuery(query, selectStatement);
	}

	@Test
	public void testBuildExpression_8()
	{
		String query = "SELECT NULLIF(e.name,) FROM Employee e";

		NullIfExpressionTester nullIf = nullIf(path("e.name"), nullExpression());
		nullIf.hasComma = true;
		nullIf.hasSpaceAfterComma = false;

		ExpressionTester selectStatement = selectStatement
		(
			select(nullIf),
			from("Employee", "e")
		);

		testQuery(query, selectStatement);
	}

	@Test
	public void testBuildExpression_9()
	{
		String query = "SELECT NULLIF(e.name, ) FROM Employee e";

		NullIfExpressionTester nullIf = nullIf(path("e.name"), nullExpression());
		nullIf.hasComma = true;
		nullIf.hasSpaceAfterComma = true;

		ExpressionTester selectStatement = selectStatement
		(
			select(nullIf),
			from("Employee", "e")
		);

		testQuery(query, selectStatement, buildFormatter_9());
	}
}