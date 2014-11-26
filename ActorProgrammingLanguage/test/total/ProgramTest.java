/*
 * @author Kyran Adams
 */
package total;

import interpreter.Interpreter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import junit.framework.AssertionFailedError;
import lexer.Lexer;
import machine.Context;
import machine.ContextException;

import org.junit.Assert;
import org.junit.Test;

import parser.ExpressionNode;
import parser.Parser;
import parser.ParserException;
import type.APNumber;
import type.APValue;

// TODO: Auto-generated Javadoc
/**
 * The Test Class TotalTest.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class ProgramTest {

    /** The number 10. */
    final static APNumber VAL_A = new APNumber("10");
    
    /** The variable named a. */
    final static String VAR_A = "a";
    
    /**
     * Test comments.
     */
    @Test
    public void testEndlineComment() {
        test("a = 10; // Hi!", VAL_A, VAR_A);
    }

    /**
     * Test separating endline comment.
     */
    @Test
    public void testSeparatingEndlineComment() {
        test("a = //8\n10;", VAL_A, VAR_A);
    }

    /**
     * Test multiline comment.
     */
    @Test
    public void testMultilineComment() {
        test("a = 10; /*\n\n\n Wow hi! */", VAL_A, VAR_A);
    }

    /**
     * Test separating multiline comment.
     */
    @Test
    public void testSeparatingMultilineComment() {
        test("a = /*\n8\n*/ 10;", VAL_A, VAR_A);
    }

    /**
     * Test statement in comment.
     */
    @Test
    public void testStatementInComment() {
        test("a = 10; //a = 8", VAL_A, VAR_A);
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
        PrintStream p = null;
        try {
            p = new PrintStream(baos, true, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding Exception");
        }
        
        new Interpreter(p).interpret(s);

        final ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        PrintStream p2 = null;
        try {
            p2 = new PrintStream(baos2, true, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding Exception");
        }

        p2.println(expected);

        try {
            Assert.assertEquals(baos2.toString("UTF-8"), baos.toString("UTF-8"));
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding Exception");
        }
    }

    /**
     * Test stack overflow error.
     *
     * @param s
     *            the s
     */
    public static void testStackOverflowError(final String s) {
        try {
            Context c = null;
            try {
                c = new Context(new PrintStream(new ByteArrayOutputStream(),
                        true, "UTF-8"));
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException("Unsupported Encoding Exception");
            }
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
            Context c = null;
            try {
                c = new Context(new PrintStream(new ByteArrayOutputStream(),
                        true, "UTF-8"));
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException("Unsupported Encoding Exception");
            }
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
            Context c = null;
            try {
                c = new Context(new PrintStream(new ByteArrayOutputStream(),
                        true, "UTF-8"));
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException("Unsupported Encoding Exception");
            }
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
     * @param expected
     *            the expected
     * @param variableName
     *            the variable name
     */
    public static void test(final String string, final Object expected,
            final String variableName) {
        Context c = null;
        try {
            c = new Context(new PrintStream(new ByteArrayOutputStream(), true,
                    "UTF-8"));
        } catch (final UnsupportedEncodingException e1) {
            throw new RuntimeException("Unsupported Encoding Exception");
        }
        final List<ExpressionNode> nodes = new Parser(new Lexer(string).lex())
        .parse(c);
        for (final ExpressionNode node : nodes) {
            node.getValue(c);
        }
        final APValue variable = c.getFunction(variableName);

        try {
            if (variable == null) {
                throw new RuntimeException("Var was null");
            }

            final Object ob = variable.getValue();
            if (ob instanceof APNumber) {
                Assert.assertTrue(((APNumber) ob)
                        .compareTo((APNumber) expected) == 0);
            } else {
                Assert.assertEquals(expected, ob);
            }
        } catch (final AssertionError e) {
            System.out.println(variable);
            throw new AssertionError("Was " + variable.getValue()
                    + " instead of " + expected, e);
        }
    }

    /**
     * Test context exception.
     *
     * @param code
     *            the code
     */
    public static void testContextException(final String code) {
        try {
            Context c = null;
            try {
                c = new Context(new PrintStream(new ByteArrayOutputStream(),
                        true, "UTF-8"));
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException("Unsupported Encoding Exception");
            }
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

    /**
     * Test no error.
     *
     * @param string
     *            the string
     */
    public static void testNoError(final String string) {
        Context c = null;
        try {
            c = new Context(new PrintStream(new ByteArrayOutputStream(), true,
                    "UTF-8"));
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding Exception");
        }
        final List<ExpressionNode> nodes = new Parser(new Lexer(string).lex())
        .parse(c);
        for (final ExpressionNode node : nodes) {
            node.getValue(c);
        }
    }
    
    public static void testArithmeticException(final String code) {
        try {
            Context c = null;
            try {
                c = new Context(new PrintStream(new ByteArrayOutputStream(),
                        true, "UTF-8"));
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException("Unsupported Encoding Exception");
            }
            final List<ExpressionNode> nodes = new Parser(new Lexer(code).lex())
                    .parse(c);
            for (final ExpressionNode node : nodes) {
                node.getValue(c);
            }
            throw new AssertionFailedError(
                    "Did not throw arithmetic exception!");
        } catch (final ArithmeticException e) {
            return;
        }
    }
}
