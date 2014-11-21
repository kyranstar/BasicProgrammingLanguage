package total;

import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class CharTest.
 */
public class CharTest {
    
    /**
     * Test adding.
     */
    @Test
    public void testAdding() {
        ProgramTest.test("a = 'a' + 1;", 'b', "a");
    }
    
    /**
     * Test adding backwards.
     */
    @Test
    public void testAddingBackwards() {
        ProgramTest.test("a =  1 + 'a';", 'b', "a");
    }

    /**
     * Test subtracting.
     */
    @Test
    public void testSubtracting() {
        ProgramTest.test("a = 'b' - 1;", 'a', "a");
    }
    
    /**
     * Test subtracting backwards.
     */
    @Test
    public void testSubtractingBackwards() {
        ProgramTest.test("a = 66 - '!';", '!', "a");
    }
    
    /**
     * Test subtracting char.
     */
    @Test
    public void testSubtractingChar() {
        ProgramTest.test("a =  'b'-'!';", 'A', "a");
    }

    /**
     * Test adding char.
     */
    @Test
    public void testAddingChar() {
        ProgramTest.test("a =  'A'+'!';", 'b', "a");
    }
    
}
