package total;

import math.APNumber;

import org.junit.Test;

public class DataTest {
    
    @Test
    public void testDeclarationWith2Params() {
        ProgramTest.testNoError("datatype Rectangle = {width, height};");
        
    }

    @Test
    public void testCreationWithTwoParams() {
        ProgramTest.testNoError("datatype Rectangle = {width, height};\n"
                + "b = new Rectangle(width=5,height=6);");
    }

    @Test
    public void testAccessOfField() {
        ProgramTest.test("datatype Rectangle = {width, height}; "
                + "b = new Rectangle(width=5,height=6); a = b.width;",
                new APNumber("5"), "a");
    }

    @Test
    public void testFunctionAcceptDataType() {
        ProgramTest.test("datatype Rectangle = {width, height}; "
                + "b = new Rectangle(width=5,height=6); "
                + "getWidth = func rect -> rect.width; a = getWidth(b);",
                new APNumber("5"), "a");
    }

    @Test
    public void testEditField() {
        ProgramTest.test("datatype Rectangle = {width, height}; "
                + "b = new Rectangle(width=5,height=6); "
                + "b.width = 7; a = b.width;", new APNumber("7"), "a");
    }
    
    @Test
    public void testAccessAndEditField() {
        ProgramTest
                .test("datatype Rectangle = {width, height}; "
                        + "b = new Rectangle(width=5,height=6); "
                        + "b.width = b.width + 2; a = b.width;", new APNumber(
                        "7"), "a");
    }
    
}
