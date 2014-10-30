package total;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import lexer.Lexer;
import machine.Context;

import org.junit.Assert;
import org.junit.Test;

import parser.ExpressionNode;
import parser.Parser;

public class TotalTest {

	final BigDecimal expected10 = new BigDecimal("10");
	final String variableNameA = "a";

	@Test
	public void testMath() {

		test("a = 10;", expected10, variableNameA);

		test("a = 9+1;", expected10, variableNameA);
		test("a = 11-1;", expected10, variableNameA);
		test("a = 5*2;", expected10, variableNameA);
		test("a = 20/2;", expected10, variableNameA);
		test("a = -20/-2;", expected10, variableNameA);
		test("a = -10/2 + 15;", expected10, variableNameA);
		test("a = -10 + 20;", expected10, variableNameA);
		test("a = 20 + -10;", expected10, variableNameA);
		test("a = 20 * 0.5;", expected10, variableNameA);
	}

	@Test
	public void testIf() {
		test("a = if true 10 else 11;", expected10, variableNameA);
		test("a = if false 11 else 10;", expected10, variableNameA);

		test("a = if 3 < 4 10 else 11;", expected10, variableNameA);
		test("a = if 3 > 4 11 else 10;", expected10, variableNameA);

		test("a = if 3 <= 3 10 else 11;", expected10, variableNameA);
		test("a = if 3 >= 3 10 else 11;", expected10, variableNameA);
	}

	@Test(expected = ArithmeticException.class)
	public void testDivideByZero() {
		test("a = 20/0;", expected10, variableNameA);
	}

	@Test
	public void testRational() {
		test("a = 10/3;", new BigDecimal("10").divide(new BigDecimal("3"),
				ExpressionNode.DivisionNode.DECIMALS, RoundingMode.HALF_UP),
				variableNameA);
	}

	private void test(final String s, final BigDecimal expected,
			final String variableName) {
		final Context c = new Context();
		final List<ExpressionNode> nodes = new Parser(new Lexer(s).lex())
				.parse(c);
		for (final ExpressionNode node : nodes) {
			System.out.println(node.getValue(c));
		}
		try {
			Assert.assertTrue(expected.compareTo((BigDecimal) c
					.getVariable(variableName).getValue(c).getValue()) == 0);
		} catch (final AssertionError e) {
			throw new AssertionError("Was "
					+ c.getVariable(variableName).getValue(c).getValue()
					+ " instead of " + expected, e);
		}
	}
}
