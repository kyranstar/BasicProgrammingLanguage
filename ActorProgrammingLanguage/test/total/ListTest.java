/*
 * @author Kyran Adams
 */
package total;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import parser.ExpressionNode.ConstantNode;
import parser.ExpressionNode.VariableNode;
import type.APValueNum;

// TODO: Auto-generated Javadoc
/**
 * The Class ListTest.
 */
public class ListTest {

    /** The Constant ONE_TWO_LIST. */
    private static final List<ConstantNode> ONE_TWO_LIST = Arrays.asList(
            new ConstantNode(new APValueNum(new BigDecimal("1"))),
            new ConstantNode(new APValueNum(new BigDecimal("2"))));
    
    /**
     * Test.
     */
    @Test
    public void test() {
        ProgramTest.test("a = [1,2];", ONE_TWO_LIST, "a");
        ProgramTest.test("a = [1] + [2];", ONE_TWO_LIST, "a");

        ProgramTest.test("b = 2; a = [1] + [b];", Arrays.asList(
                new ConstantNode(new APValueNum(new BigDecimal("1"))),
                new VariableNode("b")), "a");
        ProgramTest.test("b = 2; c = [1] + [b]; a = c{1};",
                new BigDecimal("2"), "a");

        ProgramTest.test("a = [1,2]{0};", new BigDecimal("1"), "a");
        
        ProgramTest.test("a = [1] + [2]{0};", new BigDecimal("1"), "a");
        ProgramTest.test("a = [1] + [[2]{0}]{0};", new BigDecimal("1"), "a");
        ProgramTest.test("a = [10,5]{1} - 4;", new BigDecimal("1"), "a");

    }
    
    /**
     * Sub test.
     */
    @Test
    public void subTest() {
        ProgramTest.test("a = sublist ([10,4,1,2], 2, 4);", ONE_TWO_LIST, "a");
        ProgramTest.test("a = sublist ([10,4,1,2], 1, 3);", Arrays.asList(
                new ConstantNode(new APValueNum(new BigDecimal("4"))),
                new ConstantNode(new APValueNum(new BigDecimal("1")))), "a");
    }
}
