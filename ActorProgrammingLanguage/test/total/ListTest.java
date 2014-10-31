package total;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Test;

import parser.ExpressionNode;
import type.APValueNum;

public class ListTest {

    @Test
    public void test() {
        ProgramTest.test("a = [1,2]", Arrays.asList(new ExpressionNode.ConstantNode(new APValueNum(new BigDecimal("1"))),new ExpressionNode.ConstantNode(new APValueNum(new BigDecimal("2")), "a");
    }
}
