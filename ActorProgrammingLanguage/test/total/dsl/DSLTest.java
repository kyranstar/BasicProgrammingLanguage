package total.dsl;

import org.junit.Test;

import total.ProgramTest;

public class DSLTest {

    @Test
    public void test() {
        ProgramTest.testNoError("gameloop{\n"//
                + "fps = 60;"//
                + "ups = 60;"//
                + "update {\n"//
                + ""//
                + "}\n"//
                + "draw image{\n"//
                + ""//
                + "}"//
                + "}");//
    }

}
