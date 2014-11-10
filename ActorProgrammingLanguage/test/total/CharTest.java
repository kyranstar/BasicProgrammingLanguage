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
    
}
