/*
 * @author Kyran Adams
 */
package total;

import interpreter.Interpreter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

import junit.framework.AssertionFailedError;
import lexer.Lexer;
import machine.Context;
import machine.ContextException;
import machine.FunctionSignature;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import parser.ExpressionNode;
import parser.Parser;
import parser.ParserException;

/**
 * The Test Class TotalTest.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class ProgramTest {

    /** The number 10. */
    final BigDecimal expected10 = new BigDecimal("10");
    
    /** The variable named a. */
    final String variableNameA = "a";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    /**
     * Test comments.
     */
    @Test
    public void testEndlineComment() {
        test("a = 10; // Hi!", expected10, variableNameA);
    }

    @Test
    public void testSeparatingEndlineComment() {
        test("a = //8\n10;", expected10, variableNameA);
    }

    @Test
    public void testMultilineComment() {
        test("a = 10; /*\n\n\n Wow hi! */", expected10, variableNameA);
    }

    @Test
    public void testSeparatingMultilineComment() {
        test("a = /*\n8\n*/ 10;", expected10, variableNameA);
    }

    @Test
    public void testStatementInComment() {
        test("a = 10; //a = 8", expected10, variableNameA);
    }
    
    /**
     * Expect output.
     *
     * @param s
     *            the code.
     * @param expected
     *            the expected output. Do not include newlines from println
     */
    public static void expectOutput(final String s, final String expected) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintStream p = new PrintStream(baos);
        
        new Interpreter(p).interpret(s);

        final ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        final PrintStream p2 = new PrintStream(baos2);

        p2.println(expected);

        Assert.assertEquals(baos2.toString(), baos.toString());
    }

    /**
     * Test stack overflow error.
     *
     * @param s
     *            the s
     */
    public static void testStackOverflowError(final String s) {
        try {
            final Context c = new Context(new PrintStream(
                    new ByteArrayOutputStream()));
            final List<ExpressionNode> nodes = new Parser(new Lexer(s).lex())
                    .parse(c);
            for (final ExpressionNode node : nodes) {
                node.getValue(c);
            }
            throw new AssertionFailedError("Did not throw StackOverflowError!");
        } catch (final StackOverflowError e) {
            return;
        }
    }
    
    /**
     * Method testIndexOutOfBoundsException.
     *
     * @param code
     *            String
     */
    public static void testIndexOutOfBoundsException(final String code) {
        try {
            final Context c = new Context(new PrintStream(
                    new ByteArrayOutputStream()));
            final List<ExpressionNode> nodes = new Parser(new Lexer(code).lex())
                    .parse(c);
            for (final ExpressionNode node : nodes) {
                node.getValue(c);
            }
            throw new AssertionFailedError(
                    "Did not throw IndexOutOfBoundsException!");
        } catch (final IndexOutOfBoundsException e) {
            return;
        }
    }
    
    /**
     * Test parser exception.
     *
     * @param code
     *            the code
     */
    public static void testParserException(final String code) {
        try {
            final Context c = new Context(new PrintStream(
                    new ByteArrayOutputStream()));
            final List<ExpressionNode> nodes = new Parser(new Lexer(code).lex())
            .parse(c);
            for (final ExpressionNode node : nodes) {
                node.getValue(c);
            }
            throw new AssertionFailedError("Did not throw parser exception!");
        } catch (final ParserException e) {
            return;
        }
    }
    
    /**
     * Test.
     *
     * @param string
     *            the string
     * @param list
     *            the list
     * @param variableName
     *            the variable name
     */
    public static void test(final String string, final Object expected,
            final String variableName) {
        final Context c = new Context(new PrintStream(
                new ByteArrayOutputStream()));
        final List<ExpressionNode> nodes = new Parser(new Lexer(string).lex())
        .parse(c);
        for (final ExpressionNode node : nodes) {
            node.getValue(c);
        }
        try {
            Assert.assertEquals(expected,
                    c.getFunction(new FunctionSignature(variableName))
                            .getValue());
        } catch (final AssertionError e) {
            throw new AssertionError("Was "
                    + c.getFunction(new FunctionSignature(variableName))
                            .getValue() + " instead of " + expected, e);
        }
    }

    public static void testContextException(final String code) {
        try {
            final Context c = new Context(new PrintStream(
                    new ByteArrayOutputStream()));
            final List<ExpressionNode> nodes = new Parser(new Lexer(code).lex())
            .parse(c);
            for (final ExpressionNode node : nodes) {
                node.getValue(c);
            }
            throw new AssertionFailedError("Did not throw context exception!");
        } catch (final ContextException e) {
            return;
        }
    }
}
