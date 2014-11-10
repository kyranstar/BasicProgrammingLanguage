package total;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Test;

import parser.ExpressionNode.ConstantNode;
import type.APValueNum;

public class FunctionTest {

    @Test
    public void testFirstClassFunc() {
        ProgramTest.testNum("f a = a(1); g b = 10; a = f (g);", new BigDecimal(
                10), "a");
    }
    
    @Test
    public void testFirstClassFunc2() {
        // g expects two params, giving it one
        ProgramTest.testParserException("f a = a(1); g b c = 10; a = f (g);");
    }
    
    @Test
    public void testLambda() {
        ProgramTest.testNum("f a = a(1); a = f (lambda b -> 10);",
                new BigDecimal(10), "a");
    }

    @Test
    public void testLambda2() {
        ProgramTest.testNum("f a = a(6,4); a = f (lambda b,c -> b+c);",
                new BigDecimal(10), "a");
    }
    
    @Test
    public void testFunctionScope() {
        ProgramTest
                .testNum(
                        "outside a = a(5)+outsideTwo(5); outsideTwo a = a+5; c = outside(lambda a -> a+3);",
                        new BigDecimal("18"), "c");
    }
    
    /**
     * Test function definition.
     */
    @Test
    public void testVariableDef() {
        ProgramTest.expectOutput("f = 10; println(f);", "10");
    }

    @Test
    public void testFuncDefOneParam() {
        ProgramTest.expectOutput("f a = a + 1 - 1; println(f(10));", "10");
    }

    @Test
    public void testFuncDefTwoParams() {
        ProgramTest.expectOutput("f a b = a + b - 1; println(f(10,1));", "10");
    }
    
    @Test
    public void testBinaryFunction() {
        ProgramTest.testNum("sum a b = a + b; a = 6 sum 4;",
                new BigDecimal(10), "a");
    }

    @Test
    public void testMap() {
        ProgramTest.test("a = map([1,2,3], lambda b -> b+1);", Arrays.asList(
                new ConstantNode(new APValueNum(new BigDecimal("2"))),
                new ConstantNode(new APValueNum(new BigDecimal("3"))),
                new ConstantNode(new APValueNum(new BigDecimal("4")))), "a");
    }
    
    @Test
    public void testMapBinary() {
        ProgramTest.test("a = [3,2,4] map lambda a -> a*2;", Arrays.asList(
                new ConstantNode(new APValueNum(new BigDecimal("6"))),
                new ConstantNode(new APValueNum(new BigDecimal("4"))),
                new ConstantNode(new APValueNum(new BigDecimal("8")))), "a");
    }

    /**
     * Test fibonacci sequence.
     */
    @Test
    public void testFib() {
        final String fib = "f a = if a = 0 0 else if a = 1 1 else f (a-1) + f (a-2);";

        ProgramTest.testNum(fib + "b = f (0);", new BigDecimal("0"), "b");
        ProgramTest.testNum(fib + "b = f (1);", new BigDecimal("1"), "b");
        ProgramTest.testNum(fib + "b = f (2);", new BigDecimal("1"), "b");
        ProgramTest.testNum(fib + "b = f (3);", new BigDecimal("2"), "b");
        ProgramTest.testNum(fib + "b = f (4);", new BigDecimal("3"), "b");
        ProgramTest.testNum(fib + "b = f (5);", new BigDecimal("5"), "b");
        ProgramTest.testNum(fib + "b = f (6);", new BigDecimal("8"), "b");
        ProgramTest.testNum(fib + "b = f (7);", new BigDecimal("13"), "b");
        
        ProgramTest.testStackOverflowError(fib + "b = f (-1);");
    }

    /**
     * Test print.
     */
    @Test
    public void testPrint() {
        ProgramTest.expectOutput("println(3);", "3");
    }
}
