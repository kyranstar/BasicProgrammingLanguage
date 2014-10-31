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
        TotalTest.test("a = 10;", expected10, variableNameA);
        TotalTest.test("a = 9+1;", expected10, variableNameA);
        TotalTest.test("a = 11-1;", expected10, variableNameA);
        TotalTest.test("a = 5*2;", expected10, variableNameA);
        TotalTest.test("a = 20/2;", expected10, variableNameA);
        TotalTest.test("a = 10^2 / 10;", expected10, variableNameA);

    }
    
    @Test
    public void decTest() {
        TotalTest.test("a = 20 * 0.5;", expected10, variableNameA);
        TotalTest.test("a = 4 * 2.5;", expected10, variableNameA);
    }
    
    @Test
    public void negTest() {
        TotalTest.test("a = -20/-2;", expected10, variableNameA);
        TotalTest.test("a = -10/2 + 15;", expected10, variableNameA);
        TotalTest.test("a = -10 + 20;", expected10, variableNameA);
        TotalTest.test("a = 20 + -10;", expected10, variableNameA);
    }
    
}
