/*
 * @author Kyran Adams
 */
package lexer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import lexer.Token.TokenType;

import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class LexerTest.
 */
public class LexerTest {

	/**
	 * Test.
	 */
	@Test
	public void test() {
		final Lexer lexer = new Lexer("b = 1+3;a = f(3);");
		final List<Token> expected = new ArrayList<>();

		expected.add(new Token(TokenType.IDENTIFIER, "b"));
		expected.add(new Token(TokenType.EQUAL, "="));
		expected.add(new Token(TokenType.NUMBER, "1"));
		expected.add(new Token(TokenType.PLUSMINUS, "+"));
		expected.add(new Token(TokenType.NUMBER, "3"));
		expected.add(new Token(TokenType.SEMI, ";"));

		expected.add(new Token(TokenType.IDENTIFIER, "a"));
		expected.add(new Token(TokenType.EQUAL, "="));
		expected.add(new Token(TokenType.IDENTIFIER, "f"));
		expected.add(new Token(TokenType.OPEN_PARENS, "("));
		expected.add(new Token(TokenType.NUMBER, "3"));
		expected.add(new Token(TokenType.CLOSE_PARENS, ")"));
		expected.add(new Token(TokenType.SEMI, ";"));
		
		assertEquals(expected, lexer.lex());
	}

}
