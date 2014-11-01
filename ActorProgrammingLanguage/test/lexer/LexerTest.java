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
        
        final LexerInformation lexInfo = new LexerInformation();
        
        expected.add(new Token(TokenType.IDENTIFIER, "b", lexInfo));
        expected.add(new Token(TokenType.EQUAL, "=", lexInfo));
        expected.add(new Token(TokenType.NUMBER, "1", lexInfo));
        expected.add(new Token(TokenType.PLUSMINUS, "+", lexInfo));
        expected.add(new Token(TokenType.NUMBER, "3", lexInfo));
        expected.add(new Token(TokenType.SEMI, ";", lexInfo));
        
        expected.add(new Token(TokenType.IDENTIFIER, "a", lexInfo));
        expected.add(new Token(TokenType.EQUAL, "=", lexInfo));
        expected.add(new Token(TokenType.IDENTIFIER, "f", lexInfo));
        expected.add(new Token(TokenType.OPEN_PARENS, "(", lexInfo));
        expected.add(new Token(TokenType.NUMBER, "3", lexInfo));
        expected.add(new Token(TokenType.CLOSE_PARENS, ")", lexInfo));
        expected.add(new Token(TokenType.SEMI, ";", lexInfo));
        
        assertEquals(expected, lexer.lex());
    }
    
}
