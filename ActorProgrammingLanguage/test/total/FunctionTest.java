package total;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Test;

import parser.ExpressionNode.ConstantNode;
import type.APValueNum;

public class FunctionTest {

    @Test
    public void functionUsingKeyword() {
        ProgramTest
        .test("toTen a = 10; a = toTen(5);", new BigDecimal(10), "a");
    }
    
    @Test
    public void testFirstClassFunc() {
        ProgramTest.test("f a = a(1); g b = 10; a = f (g);",
                new BigDecimal(10), "a");
    }
    
    @Test
    public void testFirstClassFunc2() {
        // g expects two params, giving it one
        ProgramTest.testParserException("f a = a(1); g b c = 10; a = f (g);");
    }
    
    @Test
    public void testLambda() {
        ProgramTest.test("f a = a(1); a = f (lambda b -> 10);", new BigDecimal(
                10), "a");
    }

    @Test
    public void testLambda2() {
        ProgramTest.test("f a = a(6,4); a = f (lambda b,c -> b+c);",
                new BigDecimal(10), "a");
    }
    
    @Test
    public void functionAsParam() {
        ProgramTest.test("f a = a(5); z a = a + 3; c = f(z);", new BigDecimal(
                "8"), "c");
    }

    @Test
    public void testFunctionScope() {
        ProgramTest
                .test("outside a = a(5)+outsideTwo(5); outsideTwo a = a+5; c = outside(lambda a -> a+3);",
                        new BigDecimal("18"), "c");
    }
    
    /**
     * Test function definition.
     */
    @Test
    public void testVariableDef() {
        ProgramTest.expectOutput("f = 10; println(f);", "10");
    }

    /**
     * Test function definition.
     */
    @Test
    public void testVariableReDef() {
        ProgramTest.expectOutput("f = 10; println(f); f = 20; println(f);",
                "10\r\n20");
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
        ProgramTest.test("sum a b = a + b; a = 6 sum 4;", new BigDecimal(10),
                "a");
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
        final String fib = "f a = if a = 0 then 0 else if a = 1 then 1 else f (a-1) + f (a-2);";

        ProgramTest.test(fib + "b = f (0);", new BigDecimal("0"), "b");
        ProgramTest.test(fib + "b = f (1);", new BigDecimal("1"), "b");
        ProgramTest.test(fib + "b = f (2);", new BigDecimal("1"), "b");
        ProgramTest.test(fib + "b = f (3);", new BigDecimal("2"), "b");
        ProgramTest.test(fib + "b = f (4);", new BigDecimal("3"), "b");
        ProgramTest.test(fib + "b = f (5);", new BigDecimal("5"), "b");
        ProgramTest.test(fib + "b = f (6);", new BigDecimal("8"), "b");
        ProgramTest.test(fib + "b = f (7);", new BigDecimal("13"), "b");
        
        ProgramTest.testStackOverflowError(fib + "b = f (-1);");
    }

    /**
     * Project euler problem 1.
     */
    @Test
    public void sumMultiples3And5Below1000() {
        ProgramTest
                .test("sum a b =a+b;\n"
                        + "modThreeFive x = if ((x%3 = 0) || (x%5 = 0)) then x else 0;\n"
                        + "result = ((1 to 999) map modThreeFive); \n"
                        + "result = result foldl sum;",
                        new BigDecimal("233168"), "result");
    }
    
    /**
     * Project euler problem 1.
     */
    @Test
    public void testFoldl() {
        ProgramTest.test("result = (1 to 5) foldl lambda x,y -> x+y;",
                new BigDecimal("15"), "result");
    }

    /**
     * Test print.
     */
    @Test
    public void testPrintNum() {
        ProgramTest.expectOutput("println(3);", "3");
    }
    
    @Test
    public void testPrintString() {
        ProgramTest.expectOutput("println(\"Hi\");", "Hi");
    }
}
