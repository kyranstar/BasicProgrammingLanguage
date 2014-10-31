package total;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import parser.ExpressionNode;
import parser.ExpressionNode.ConstantNode;
import type.APValueNum;

public class ListTest {

    private static final List<ConstantNode> ONE_TWO_LIST = Arrays
            .asList(new ExpressionNode.ConstantNode(new APValueNum(
                    new BigDecimal("1"))), new ExpressionNode.ConstantNode(
                    new APValueNum(new BigDecimal("2"))));
    
    @Test
    public void test() {
        ProgramTest.test("a = [1,2];", ONE_TWO_LIST, "a");
        ProgramTest.test("a = [1] + [2];", ONE_TWO_LIST, "a");

        ProgramTest.test("a = [1,2](0);", new BigDecimal("1"), "a");
    }
}
