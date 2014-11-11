package total;

import org.junit.Test;

public class CharTest {
    
    @Test
    public void testAdding() {
        ProgramTest.test("a = 'a' + 1;", 'b', "a");
    }
    
    @Test
    public void testAddingBackwards() {
        ProgramTest.test("a =  1 + 'a';", 'b', "a");
    }

    @Test
    public void testSubtracting() {
        ProgramTest.test("a = 'b' - 1;", 'a', "a");
    }
    
    @Test
    public void testSubtractingBackwards() {
        ProgramTest.test("a = 66 - '!';", '!', "a");
    }
    
    @Test
    public void testSubtractingChar() {
        ProgramTest.test("a =  'b'-'!';", 'A', "a");
    }

    @Test
    public void testAddingChar() {
        ProgramTest.test("a =  'A'+'!';", 'b', "a");
    }
    
}
