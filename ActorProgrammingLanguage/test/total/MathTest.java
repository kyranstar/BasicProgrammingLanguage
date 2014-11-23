/*
 * @author Kyran Adams
 */
package total;

import java.math.RoundingMode;

import org.junit.Test;

import type.APNumber;
import type.APValueNum;

// TODO: Auto-generated Javadoc
/**
 * The Class MathTest.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class MathTest {
    /** The number 10. */
    final APNumber expected10 = new APNumber("10");
    
    /** The variable named a. */
    final String variableNameA = "a";

    /**
     * Int test.
     */
    @Test
    public void intTest() {
        ProgramTest.test("a = 10;", expected10, variableNameA);
        ProgramTest.test("a = 9+1;", expected10, variableNameA);
        ProgramTest.test("a = 11-1;", expected10, variableNameA);
        ProgramTest.test("a = 5*2;", expected10, variableNameA);
        ProgramTest.test("a = 20/2;", expected10, variableNameA);
        ProgramTest.test("a = 10^2 / 10;", expected10, variableNameA);
        ProgramTest.test("a = 1000/10^2;", expected10, variableNameA);

    }

    /**
     * Parens test.
     */
    @Test
    public void parensTest() {
        ProgramTest.test("a = 7+1*3;", expected10, variableNameA);
        ProgramTest.test("a = (3+2)*2;", expected10, variableNameA);
        ProgramTest.test("a = (8+2)^2;", new APNumber("100"), variableNameA);
        ProgramTest.test("a = 6+(2^2);", expected10, variableNameA);
    }

    /**
     * functions test.
     */
    @Test
    public void funcTest() {
        ProgramTest.test("a = sqrt(100);", expected10, variableNameA);

    }
    
    /**
     * Dec test.
     */
    @Test
    public void decTest() {
        ProgramTest.test("a = 20 * 0.5;", expected10, variableNameA);
        ProgramTest.test("a = 4 * 2.5;", expected10, variableNameA);
    }
    
    /**
     * Neg test.
     */
    @Test
    public void negTest() {
        ProgramTest.test("a = -20/-2;", expected10, variableNameA);
        ProgramTest.test("a = -10/2 + 15;", expected10, variableNameA);
        ProgramTest.test("a = -10 + 20;", expected10, variableNameA);
        ProgramTest.test("a = 20 + -10;", expected10, variableNameA);
        ProgramTest.test("a = (100 ^ -2) * 100000;", expected10, variableNameA);
        ProgramTest.test("a = 100 ^ (1/2);", expected10, variableNameA);
        ProgramTest.test("a = 110 - 10^2;", expected10, variableNameA);
    }
    
    /**
     * Invalid type test.
     */
    @Test
    public void invalidTypeTest() {
        ProgramTest.testParserException("a = true + false;");
        ProgramTest.testParserException("a = 3 + false;");
        ProgramTest.testParserException("a = true + 3;");
        ProgramTest.testParserException("a = 3 && 4;");
        ProgramTest.testParserException("a = 4 && false;");
        ProgramTest.testParserException("a = true && 3;");
    }
    
    /**
     * Pow test.
     */
    @Test
    public void powTest() {
        ProgramTest.testNoError("a = 100^-798;");
        ProgramTest.testNoError("a = 100^4438;");
        ProgramTest.testNoError("a = 100^-1;");
        ProgramTest.testNoError("a = 100^0;");
        ProgramTest.testNoError("a = 100^1;");
        ProgramTest.testNoError("a = 100^2;");
    }
    
    /**
     * Test divide by zero.
     */
    public void testDivideByZero() {
        ProgramTest.testArithmeticException("a = 20/0;", expected10,
                variableNameA);
    }
    
    /**
     * Test rational.
     */
    @Test
    public void testRational() {
        ProgramTest
        .test("a = 10/3;", new APNumber("10").divide(new APNumber("3"),
                APValueNum.DECIMALS, RoundingMode.HALF_UP),
                variableNameA);
    }
    
    /**
     * Test mod.
     */
    @Test
    public void testMod() {
        ProgramTest.test("a = 5 % 3;", new APNumber("2"), variableNameA);
    }
}
