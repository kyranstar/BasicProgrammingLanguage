package total;

import java.math.BigDecimal;

import org.junit.Test;

public class BoolTest {
    /** The number 10. */
    final BigDecimal expected10 = new BigDecimal("10");

    /** The variable named a. */
    final String variableNameA = "a";
    
    @Test
    public void testIfCondition() {
        ProgramTest.testNum("a = if true 10 else 11;", expected10,
                variableNameA);
    }
    
    @Test
    public void testIfNegativeCondition() {
        ProgramTest.testNum("a = if false 11 else 10;", expected10,
                variableNameA);
    }
    
    @Test
    public void testLessThan() {
        ProgramTest.testNum("a = if 3 < 4 10 else 11;", expected10,
                variableNameA);
    }
    
    @Test
    public void testGreaterThan() {
        ProgramTest.testNum("a = if 3 > 4 11 else 10;", expected10,
                variableNameA);
    }
    
    @Test
    public void testLessThanEquals() {
        ProgramTest.testNum("a = if 3 <= 3 10 else 11;", expected10,
                variableNameA);
    }
    
    @Test
    public void testGreaterThanEquals() {
        ProgramTest.testNum("a = if 3 >= 3 10 else 11;", expected10,
                variableNameA);
    }
}
