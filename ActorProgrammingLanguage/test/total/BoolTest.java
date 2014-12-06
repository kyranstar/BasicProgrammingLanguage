/*
 * 
 */
package total;

import org.junit.Test;

import type.APNumber;

// TODO: Auto-generated Javadoc
/**
 * The Class BoolTest.
 */
public class BoolTest {
    /** The number 10. */
    final static APNumber VAL_10 = new APNumber("10");
    
    /** The variable named a. */
    final static String VAR_A = "a";

    /**
     * Test if condition.
     */
    @Test
    public void testIfCondition() {
        ProgramTest.test("a = if true then 10 else 11;", VAL_10, VAR_A);
    }

    /**
     * Test if negative condition.
     */
    @Test
    public void testIfNegativeCondition() {
        ProgramTest.test("a = if false then 11 else 10;", VAL_10, VAR_A);
    }

    /**
     * Test less than.
     */
    @Test
    public void testLessThan() {
        ProgramTest.test("a = if 3 < 4 then 10 else 11;", VAL_10, VAR_A);
    }

    /**
     * Test greater than.
     */
    @Test
    public void testGreaterThan() {
        ProgramTest.test("a = if 3 > 4 then 11 else 10;", VAL_10, VAR_A);
    }

    /**
     * Test less than equals.
     */
    @Test
    public void testLessThanEquals() {
        ProgramTest.test("a = if 3 <= 3 then 10 else 11;", VAL_10, VAR_A);
    }

    /**
     * Test greater than equals.
     */
    @Test
    public void testGreaterThanEquals() {
        ProgramTest.test("a = if 3 >= 3 then 10 else 11;", VAL_10, VAR_A);
        ProgramTest.test("a = if 3 >= -3 then 10 else 11;", VAL_10, VAR_A);
        ProgramTest.test("a = if 3 >= (-3+5) then 10 else 11;", VAL_10, VAR_A);
    }

    /**
     * Test equals.
     */
    @Test
    public void testEquals() {
        ProgramTest.test("a = true = true;", true, VAR_A);
        ProgramTest.test("a = false = false;", true, VAR_A);
        ProgramTest.test("a = false = true;", false, VAR_A);
        ProgramTest.test("a = true = false;", false, VAR_A);
        ProgramTest.test("a = 4 = 4;", true, VAR_A);
        ProgramTest.test("a = 3 = 4;", false, VAR_A);
    }

    /**
     * Test and.
     */
    @Test
    public void testAnd() {
        ProgramTest.test("a = true && true;", true, VAR_A);
        ProgramTest.test("a = true && false;", false, VAR_A);
        ProgramTest.test("a = false && true;", false, VAR_A);
        ProgramTest.test("a = false && false;", false, VAR_A);
    }
    
    /**
     * Test or.
     */
    @Test
    public void testOr() {
        ProgramTest.test("a = true || true;", true, VAR_A);
        ProgramTest.test("a = true || false;", true, VAR_A);
        ProgramTest.test("a = false || true;", true, VAR_A);
        ProgramTest.test("a = false || false;", false, VAR_A);
    }
}
