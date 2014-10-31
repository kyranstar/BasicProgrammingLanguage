/*
 * @author Kyran Adams
 */
package total;

import interpreter.Interpreter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import lexer.Lexer;
import machine.Context;

import org.junit.Assert;
import org.junit.Test;

import parser.ExpressionNode;
import parser.Parser;
import parser.ParserException;

// TODO: Auto-generated Javadoc
/**
 * The Test Class TotalTest.
 */
public class ProgramTest {

    /** The number 10. */
    final BigDecimal expected10 = new BigDecimal("10");
    
    /** The variable named a. */
    final String variableNameA = "a";

    /**
     * Test if.
     */
    @Test
    public void testIf() {
        test("a = if true 10 else 11;", expected10, variableNameA);
        test("a = if false 11 else 10;", expected10, variableNameA);

        test("a = if 3 < 4 10 else 11;", expected10, variableNameA);
        test("a = if 3 > 4 11 else 10;", expected10, variableNameA);

        test("a = if 3 <= 3 10 else 11;", expected10, variableNameA);
        test("a = if 3 >= 3 10 else 11;", expected10, variableNameA);
    }

    /**
     * Test function definition.
     */
    @Test
    public void testFuncDef() {
        expectOutput("f = 10; println(f);", "10");
        expectOutput("f a = a + 1 - 1; println(f(10));", "10");
        expectOutput("f a b = a + b - 1; println(f(10,1));", "10");
    }

    /**
     * Test fibonacci sequence.
     */
    @Test
    public void testFib() {
        final String fib = "f a = if a = 0 0 else if a = 1 1 else f (a-1) + f (a-2);";
        
        test(fib + "b = f (0);", new BigDecimal("0"), "b");
        test(fib + "b = f (1);", new BigDecimal("1"), "b");
        test(fib + "b = f (2);", new BigDecimal("1"), "b");
        test(fib + "b = f (3);", new BigDecimal("2"), "b");
        test(fib + "b = f (4);", new BigDecimal("3"), "b");
        test(fib + "b = f (5);", new BigDecimal("5"), "b");
        test(fib + "b = f (6);", new BigDecimal("8"), "b");
        test(fib + "b = f (7);", new BigDecimal("13"), "b");

        testStackOverflowError(fib + "b = f (-1);");
    }
    
    /**
     * Test print.
     */
    @Test
    public void testPrint() {
        expectOutput("println(3);", "3");
    }

    /**
     * Test divide by zero.
     */
    @Test(expected = ArithmeticException.class)
    public void testDivideByZero() {
        test("a = 20/0;", expected10, variableNameA);
    }

    /**
     * Test rational.
     */
    @Test
    public void testRational() {
        test("a = 10/3;", new BigDecimal("10").divide(new BigDecimal("3"),
                ExpressionNode.DivisionNode.DECIMALS, RoundingMode.HALF_UP),
                variableNameA);
        
        Assert.fail();
    }

    /**
     * Runs code s. Tests whether value expected is stored in variable
     * variableName.
     *
     * @param s
     *            the code
     * @param expected
     *            the expected value
     * @param variableName
     *            the variable name
     */
    public static void test(final String s, final BigDecimal expected,
            final String variableName) {
        final Context c = new Context(new PrintStream(
                new ByteArrayOutputStream()));
        final List<ExpressionNode> nodes = new Parser(new Lexer(s).lex())
        .parse(c);
        for (final ExpressionNode node : nodes) {
            node.getValue(c);
        }
        try {
            Assert.assertTrue(expected.compareTo((BigDecimal) c.getVariable(
                    variableName).getValue()) == 0);
        } catch (final AssertionError e) {
            throw new AssertionError("Was "
                    + c.getVariable(variableName).getValue() + " instead of "
                    + expected, e);
        }
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

    public static void testParserException(final String s) {
        try {
            final Context c = new Context(new PrintStream(
                    new ByteArrayOutputStream()));
            final List<ExpressionNode> nodes = new Parser(new Lexer(s).lex())
                    .parse(c);
            for (final ExpressionNode node : nodes) {
                node.getValue(c);
            }
        } catch (final ParserException e) {
            return;
        }
        throw new AssertionError("Should have thrown a parser exception!");
    }

    private void testStackOverflowError(final String code) {
        try {
            final Context c = new Context(new PrintStream(
                    new ByteArrayOutputStream()));
            final List<ExpressionNode> nodes = new Parser(new Lexer(code).lex())
                    .parse(c);
            for (final ExpressionNode node : nodes) {
                node.getValue(c);
            }
        } catch (final StackOverflowError e) {
            return;
        }
        throw new AssertionError(
                "Should have thrown a stack overflow exception!");
    }
}
