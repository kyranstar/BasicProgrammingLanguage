package total;

import java.math.BigDecimal;

import org.junit.Test;

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
