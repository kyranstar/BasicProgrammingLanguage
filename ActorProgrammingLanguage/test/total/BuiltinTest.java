package total;

import java.util.Arrays;

import org.junit.Test;

import parser.ExpressionNode.ConstantNode;
import type.APNumber;
import type.APValueNum;

/**
 * Test built in functions
 * 
 * @author s-KADAMS
 */
public class BuiltinTest {

    /**
     * Test isa function.
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
     * Test foreach.
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
     * Test foldl.
     */
    @Test
    public void testFoldl() {
        ProgramTest.test("result = (1 to 5) foldl func x y -> x+y;",
                new APNumber("15"), "result");
    }
    
    /**
     * Test print with a number.
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
