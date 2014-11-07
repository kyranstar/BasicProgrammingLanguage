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
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
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

    @Test
    public void testNumber() {
        final Lexer lexer = new Lexer("1 1.0 .0 -1.0");
        final List<Token> expected = new ArrayList<>();
        final LexerInformation lexInfo = new LexerInformation();
        
        expected.add(new Token(TokenType.NUMBER, "1", lexInfo));
        expected.add(new Token(TokenType.NUMBER, "1.0", lexInfo));
        expected.add(new Token(TokenType.NUMBER, ".0", lexInfo));
        expected.add(new Token(TokenType.PLUSMINUS, "-", lexInfo));
        expected.add(new Token(TokenType.NUMBER, "1.0", lexInfo));
        assertEquals(expected, lexer.lex());
    }

    @Test
    public void testBoolean() {
        final Lexer lexer = new Lexer("true false");
        final List<Token> expected = new ArrayList<>();
        final LexerInformation lexInfo = new LexerInformation();
        
        expected.add(new Token(TokenType.BOOLEAN, "true", lexInfo));
        expected.add(new Token(TokenType.BOOLEAN, "false", lexInfo));
        assertEquals(expected, lexer.lex());
    }
    
    @Test
    public void testString() {
        final Lexer lexer = new Lexer("\"\" \"hi\"");
        final List<Token> expected = new ArrayList<>();
        final LexerInformation lexInfo = new LexerInformation();
        
        expected.add(new Token(TokenType.STRING, "", lexInfo));
        expected.add(new Token(TokenType.STRING, "hi", lexInfo));
        assertEquals(expected, lexer.lex());
    }
    
}
