package total;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import lexer.Lexer;
import machine.Context;

import org.junit.Test;

import parser.ExpressionNode;
import parser.Parser;

public class TotalTest {
	
	@Test
	public void test() {
		final BigDecimal expected = new BigDecimal("10");
		
		test("a = 10;", expected);
		
		test("a = 9+1;", expected);
		test("a = 11-1;", expected);
		test("a = 5*2;", expected);
		test("a = 20/2;", expected);
		test("a = -10+20;", expected);
		test("a = 20 + -10;", expected);
		test("a = 20*0.5;", expected);
		
	}

	private void test(final String s, final BigDecimal expected) {
		final Context c = new Context();
		final List<ExpressionNode> nodes = new Parser(new Lexer(s).lex()).parse(c);
		for (final ExpressionNode node : nodes) {
			System.out.println(node.getValue(c));
		}
		assertEquals(expected.stripTrailingZeros(), ((BigDecimal) c.getVariable("a").getValue(c).getValue()).stripTrailingZeros());
	}
	
}
