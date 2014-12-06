/*
 * 
 */
package total;

import org.junit.Test;

import type.APNumber;

// TODO: Auto-generated Javadoc
/**
 * The Class DataTest.
 */
public class DataTest {

    /**
     * Test declaration with2 params.
     */
    @Test
    public void testDeclarationWith2Params() {
        ProgramTest.testNoError("datatype Rectangle = Rect {width, height};");

    }
    
    /**
     * Test declaration with2 constructors.
     */
    @Test
    public void testDeclarationWith2Constructors() {
        ProgramTest
                .testNoError("datatype Rectangle = Rect {width, height} | Square {size};");

    }
    
    /**
     * Test enumeration type.
     */
    @Test
    public void testEnumerationType() {
        ProgramTest
                .test("datatype Boolean = True | False; b = new Boolean.True(); a = if b isa Boolean$True then true else false;",
                        true, "a");
        ProgramTest
                .test("datatype Boolean = True | False; b = new Boolean.False(); a = if b isa Boolean$True then true else false;",
                        false, "a");
    }
    
    /**
     * Test type function.
     */
    @Test
    public void testTypeFunction() {
        // Is not exactly equal
        ProgramTest
                .test("datatype Boolean = True | False; b = new Boolean.True(); a = (type(b) = Boolean);",
                        false, "a");
        ProgramTest
        .test("datatype Boolean = True | False; b = new Boolean.True(); a = (type(b) = Boolean$True);",
                true, "a");
        ProgramTest
                .test("datatype Boolean = True | False; b = new Boolean.True(); a = type(b) = Boolean$False;",
                        false, "a");
        ProgramTest.test("a = type(5) = Num;", true, "a");
        ProgramTest.test("a = type(\"Hi\") = List;", true, "a");
        ProgramTest.test("a = type('H') = Char;", true, "a");
        ProgramTest.test("a = type(true) = Bool;", true, "a");

    }

    /**
     * Test creation with two constructors.
     */
    @Test
    public void testCreationWithTwoConstructors() {
        ProgramTest
        .testNoError("datatype Rectangle = Rect {width, height}| Square {size};\n"
                + "b = new Rectangle.Rect(width=5,height=6);"
                + "c = new Rectangle.Square(size = 7);");
    }

    /**
     * Test creation with two params.
     */
    @Test
    public void testCreationWithTwoParams() {
        ProgramTest.testNoError("datatype Rectangle = Rect {width, height};\n"
                + "b = new Rectangle.Rect(width=5,height=6);");
    }
    
    /**
     * Test access of field two constructors.
     */
    @Test
    public void testAccessOfFieldTwoConstructors() {
        ProgramTest.test(
                "datatype Rectangle = Rect {width, height} | Square {size}; "
                        + "b = new Rectangle.Square(size = 5); a = b.size;",
                        new APNumber("5"), "a");
    }
    
    /**
     * Test access of field.
     */
    @Test
    public void testAccessOfField() {
        ProgramTest.test("datatype Rectangle = Rect {width, height}; "
                + "b = new Rectangle.Rect(width=5,height=6); a = b.width;",
                new APNumber("5"), "a");
    }
    
    /**
     * Test function accept data type.
     */
    @Test
    public void testFunctionAcceptDataType() {
        ProgramTest.test("datatype Rectangle = Rect {width, height}; "
                + "b = new Rectangle.Rect(width=5,height=6); "
                + "getWidth = func rect -> rect.width; a = getWidth(b);",
                new APNumber("5"), "a");
    }
    
    /**
     * Test edit field.
     */
    @Test
    public void testEditField() {
        ProgramTest.test("datatype Rectangle = Rect {width, height}; "
                + "b = new Rectangle.Rect(width=5,height=6); "
                + "b.width = 7; a = b.width;", new APNumber("7"), "a");
    }

    /**
     * Test access and edit field.
     */
    @Test
    public void testAccessAndEditField() {
        ProgramTest
        .test("datatype Rectangle = Rect {width, height}; "
                + "b = new Rectangle.Rect(width=5,height=6); "
                + "b.width = b.width + 2; a = b.width;", new APNumber(
                        "7"), "a");
    }
    
    /**
     * Test declaration no args.
     */
    @Test
    public void testDeclarationNoArgs() {
        ProgramTest
        .testNoError("datatype Rectangle = Rect {width, height} | Default; "
                + "b = new Rectangle.Default(); ");
    }
}
