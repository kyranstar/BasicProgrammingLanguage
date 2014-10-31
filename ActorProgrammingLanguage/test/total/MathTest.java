package total;

import java.math.BigDecimal;

import org.junit.Test;

public class MathTest {
    /** The number 10. */
    final BigDecimal expected10 = new BigDecimal("10");
    
    /** The variable named a. */
    final String variableNameA = "a";

    @Test
    public void intTest() {
        ProgramTest.test("a = 10;", expected10, variableNameA);
        ProgramTest.test("a = 9+1;", expected10, variableNameA);
        ProgramTest.test("a = 11-1;", expected10, variableNameA);
        ProgramTest.test("a = 5*2;", expected10, variableNameA);
        ProgramTest.test("a = 20/2;", expected10, variableNameA);
        ProgramTest.test("a = 10^2 / 10;", expected10, variableNameA);

    }
    
    @Test
    public void decTest() {
        ProgramTest.test("a = 20 * 0.5;", expected10, variableNameA);
        ProgramTest.test("a = 4 * 2.5;", expected10, variableNameA);
    }
    
    @Test
    public void negTest() {
        ProgramTest.test("a = -20/-2;", expected10, variableNameA);
        ProgramTest.test("a = -10/2 + 15;", expected10, variableNameA);
        ProgramTest.test("a = -10 + 20;", expected10, variableNameA);
        ProgramTest.test("a = 20 + -10;", expected10, variableNameA);
        ProgramTest.test("a = 100 ^ -2 * 100000;", expected10, variableNameA);
        ProgramTest.test("a = 100 ^ (1/2);", expected10, variableNameA);
    }
    
    @Test
    public void invalidTypeTest() {
        ProgramTest.testParserException("a = true + false");
        ProgramTest.testParserException("a = 3 + false");
        ProgramTest.testParserException("a = true + 3");
        ProgramTest.testParserException("a = 3 && 4");
        ProgramTest.testParserException("a = 4 && false");
        ProgramTest.testParserException("a = true && 3");
    }
    
}
