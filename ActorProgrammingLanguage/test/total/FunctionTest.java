package total;

import java.math.BigDecimal;

import org.junit.Test;

public class FunctionTest {

    @Test
    public void test() {
        ProgramTest.testNum("f a = a(1); g b = 10; a = f (g);", new BigDecimal(
                10), "a");
    }

}
