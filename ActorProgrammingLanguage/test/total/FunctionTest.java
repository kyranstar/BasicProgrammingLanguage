/*
 *
 */
package total;

import java.util.Arrays;

import org.junit.Test;

import parser.ExpressionNode.ConstantNode;
import type.APNumber;
import type.APValueNum;

// TODO: Auto-generated Javadoc
/**
 * The Class FunctionTest.
 */
public class FunctionTest {

    /**
     * Function using keyword.
     */
    @Test
    public void functionUsingKeyword() {
        ProgramTest.test("toTen = func a -> 10; a = toTen(5);",
                new APNumber(10), "a");
    }
    
    /**
     * Test isa.
     */
    @Test
    public void testIsa() {
        ProgramTest.test("a = 10 isa Num;", true, "a");
        ProgramTest.test("a = 10 isa Bool;", false, "a");
        ProgramTest.test("a = true isa Bool;", true, "a");
        ProgramTest.test("a = true isa Num;", false, "a");
        ProgramTest.test("a = [] isa List;", true, "a");
        ProgramTest.test("a = [] isa Bool;", false, "a");
        ProgramTest.test("a = (func a -> 10) isa Func;", true, "a");
        ProgramTest.test("a = (func a -> 10) isa Bool;", false, "a");
    }
    
    /**
     * Test isa datatype.
     */
    @Test
    public void testIsaDatatype() {
        final String decl = "datatype Rectangle = Rect{width, height} | Square {size};";
        ProgramTest.test(decl + "b = new Rectangle.Rect(width=5,height=6);"
                + "a = b isa Rectangle$Rect;", true, "a");
        ProgramTest.test(decl + "b = new Rectangle.Square(size=4);"
                + "a = b isa Rectangle$Square;", true, "a");
        ProgramTest.test(decl + "b = new Rectangle.Rect(width=5,height=6);"
                + "a = b isa Rectangle$Square;", false, "a");
        ProgramTest.test(decl + "b = new Rectangle.Square(size=4);"
                + "a = b isa Rectangle$Rect;", false, "a");
        
        ProgramTest.test(decl + "b = new Rectangle.Rect(width=5,height=6);"
                + "a = b isa Rectangle;", true, "a");
        ProgramTest.test(decl + "b = new Rectangle.Square(size=4);"
                + "a = b isa Rectangle;", true, "a");

    }

    /**
     * Test mutability.
     */
    @Test
    public void testMutability() {
        ProgramTest.testParserException("f = 10; f = 5;");
        ProgramTest.test("mut f = 10; f = 5;", new APNumber("5"), "f");
    }

    /**
     * Test first class func.
     */
    @Test
    public void testFirstClassFunc() {
        ProgramTest.test("f = func a -> a(1); g = func b -> 10; a = f (g);",
                new APNumber(10), "a");
    }
    
    /**
     * Test non alphabetic identifiers.
     */
    @Test
    public void testNonAlphabeticIdentifiers() {
        ProgramTest.test("!! = func a b -> a{b}; a = [1,2,3] !! 0;",
                new APNumber(1), "a");
    }
    
    /**
     * Test first class func2.
     */
    @Test
    public void testFirstClassFunc2() {
        // g expects two params, giving it one
        ProgramTest.testParserException("f a = a(1); g b c = 10; a = f (g);");
    }

    /**
     * Test sequence.
     */
    @Test
    public void testSequence() {
        // g expects two params, giving it one
        ProgramTest.test("a = {print(5); println(4); return 6;};",
                new APNumber("6"), "a");
        ProgramTest
                .expectOutput("a = {print(5); println(4); return 6;};", "54");
        ProgramTest.expectOutput(
                "a = {print(5); print(4); return 6;}; println(3);", "543");
    }
    
    /**
     * Test lambda.
     */
    @Test
    public void testLambda() {
        ProgramTest.test("f = func a -> a(1); a = f (func b -> 10);",
                new APNumber(10), "a");
    }

    /**
     * Test lambda2.
     */
    @Test
    public void testLambda2() {
        ProgramTest.test("f = func a -> a(6,4); a = f (func b c -> b+c);",
                new APNumber(10), "a");
    }
    
    /**
     * Function as param.
     */
    @Test
    public void functionAsParam() {
        ProgramTest.test("f = func a -> a(5); z = func a -> a + 3; c = f(z);",
                new APNumber("8"), "c");
    }

    /**
     * Test function scope.
     */
    @Test
    public void testFunctionScope() {
        ProgramTest
                .test("outside = func a -> a(5)+outsideTwo(5); outsideTwo = func a -> a+5; c = outside(func a -> a+3);",
                        new APNumber("18"), "c");
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
        ProgramTest.expectOutput("mut f = 10; println(f); f = 20; println(f);",
                "10\r\n20");
        
    }

    /**
     * Test func def one param.
     */
    @Test
    public void testFuncDefOneParam() {
        ProgramTest.expectOutput("f = func a -> a + 1 - 1; println(f(10));",
                "10");
    }

    /**
     * Test func def two params.
     */
    @Test
    public void testFuncDefTwoParams() {
        ProgramTest.expectOutput(
                "f = func a b -> a + b - 1; println(f(10,1));", "10");
    }
    
    /**
     * Test binary function.
     */
    @Test
    public void testBinaryFunction() {
        ProgramTest.test("sum = func a b -> a + b; a = 6 sum 4;", new APNumber(
                10), "a");
    }
    
    /**
     * Test in function.
     */
    @Test
    public void testIn() {
        ProgramTest
                .expectOutput(
                        "println(toString(1 in [1,2,3]) + [' '] + toString(1 in [2,3,4]));",
                        "true false");
    }

    /**
     * Test length function.
     */
    @Test
    public void testLength() {
        ProgramTest
                .expectOutput(
                        "println(toString(length ([1,2,3])) + [' '] + toString(length ([])));",
                        "3 0");
    }
    
    /**
     * Test map.
     */
    @Test
    public void testMap() {
        ProgramTest.test("a = map([1,2,3], func b -> b+1);", Arrays.asList(
                new ConstantNode(new APValueNum(new APNumber("2"))),
                new ConstantNode(new APValueNum(new APNumber("3"))),
                new ConstantNode(new APValueNum(new APNumber("4")))), "a");
    }
    
    /**
     * Test map.
     */
    @Test
    public void testForeach() {
        ProgramTest.expectOutput("foreach([1,2,3], println);", "1\r\n2\r\n3");
    }
    
    /**
     * Test map binary.
     */
    @Test
    public void testMapBinary() {
        ProgramTest.test("a = [3,2,4] map func a -> a*2;", Arrays.asList(
                new ConstantNode(new APValueNum(new APNumber("6"))),
                new ConstantNode(new APValueNum(new APNumber("4"))),
                new ConstantNode(new APValueNum(new APNumber("8")))), "a");
    }

    /**
     * Test fibonacci sequence.
     */
    @Test
    public void testFib() {
        final String fib = "f = func a -> if a = 0 then 0 else if a = 1 then 1 else f (a-1) + f (a-2);";

        ProgramTest.test(fib + "b = f (0);", new APNumber("0"), "b");
        ProgramTest.test(fib + "b = f (1);", new APNumber("1"), "b");
        ProgramTest.test(fib + "b = f (2);", new APNumber("1"), "b");
        ProgramTest.test(fib + "b = f (3);", new APNumber("2"), "b");
        ProgramTest.test(fib + "b = f (4);", new APNumber("3"), "b");
        ProgramTest.test(fib + "b = f (5);", new APNumber("5"), "b");
        ProgramTest.test(fib + "b = f (6);", new APNumber("8"), "b");
        ProgramTest.test(fib + "b = f (7);", new APNumber("13"), "b");
        
        ProgramTest.testStackOverflowError(fib + "b = f (-1);");
    }

    /**
     * Project euler problem 1.
     */
    @Test
    public void euler1() {
        ProgramTest
                .test("sum  = func a b -> a+b;\n"
                        + "modThreeFive = func x -> if ((x%3 = 0) || (x%5 = 0)) then x else 0;\n"
                        + "mut result = (1 to 999) map modThreeFive; \n"
                        + "result = result foldl sum;", new APNumber("233168"),
                "result");
    }
    
    /**
     * Project euler problem 6.
     */
    @Test
    public void euler6() {
        ProgramTest.test("sum = func a b -> a+b;\n"
                + "sumOfSquares = (1 to 10) foldl func a b -> a + b^2;\n"
                + "squareOfSums = ((1 to 10) foldl func a b -> a + b)^2;\n"
                + "diff = squareOfSums - sumOfSquares;", new APNumber("2640"),
                "diff");
    }
    
    /**
     * Test foldl.
     */
    @Test
    public void testFoldl() {
        ProgramTest.test("result = (1 to 5) foldl func x y -> x+y;",
                new APNumber("15"), "result");
    }

    /**
     * Test print.
     */
    @Test
    public void testPrintNum() {
        ProgramTest.expectOutput("println(3);", "3");
    }
    
    /**
     * Test print string.
     */
    @Test
    public void testPrintString() {
        ProgramTest.expectOutput("println(\"Hi\");", "Hi");
    }
}
