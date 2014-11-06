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
import type.APValueChar;
import type.APValueNum;

// TODO: Auto-generated Javadoc
/**
 * The Class ListTest.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class ListTest {

    /** The Constant ONE_TWO_LIST. */
    private static final List<ConstantNode> ONE_TWO_LIST = Arrays.asList(
            new ConstantNode(new APValueNum(new BigDecimal("1"))),
            new ConstantNode(new APValueNum(new BigDecimal("2"))));

    @Test
    public void indexOperatorAndConcat() {
        ProgramTest.testNum("a = ([1] + [[2]{0}]){0};", new BigDecimal("1"),
                "a");
    }

    @Test
    public void concatAndIndex() {
        ProgramTest.testNum("a = ([1] + [2]){0};", new BigDecimal("1"), "a");
    }

    @Test
    public void indexOperator2() {
        ProgramTest.testNum("a = [1,2]{0};", new BigDecimal("1"), "a");
    }

    @Test
    public void listFunctionReturningParameter() {
        ProgramTest.testNum("f a = [a,a,a]; c = f(3){1};", new BigDecimal("3"),
                "c");
    }

    @Test
    public void indexOperator() {
        ProgramTest.testNum("b = 2; c = [1] + [b]; a = c{1};", new BigDecimal(
                "2"), "a");
    }

    @Test
    public void variableList() {
        ProgramTest.test("b = 2; a = [1] + [b];", Arrays.asList(
                new ConstantNode(new APValueNum(new BigDecimal("1"))),
                new VariableNode("b")), "a");
    }

    @Test
    public void listConcat() {
        ProgramTest.test("a = [1] + [2];", ONE_TWO_LIST, "a");
    }

    @Test
    public void listDeclaration() {
        ProgramTest.test("a = [1,2];", ONE_TWO_LIST, "a");
    }

    @Test
    public void sublistDoubleParam2() {
        ProgramTest.test("a = sublist ([10,4,1,2], 1, 3);", Arrays.asList(
                new ConstantNode(new APValueNum(new BigDecimal("4"))),
                new ConstantNode(new APValueNum(new BigDecimal("1")))), "a");
    }

    @Test
    public void sublistDoubleParam() {
        ProgramTest.test("a = sublist ([10,4,1,2], 2, 4);", ONE_TWO_LIST, "a");
    }
    
    @Test
    public void sublistSingleParam2() {
        ProgramTest.test("a = sublist ([10,1,2], 1);", ONE_TWO_LIST, "a");
    }
    
    @Test
    public void sublistSingleParam() {
        ProgramTest.test("a = sublist ([10,4,1,2], 2);", ONE_TWO_LIST, "a");
    }
    
    @Test
    public void sublistTooHighSingleParam() {
        ProgramTest.testIndexOutOfBoundsException("a = sublist ([10,1,2], 4);");
    }
    
    @Test
    public void subListNegativeSingleParam() {
        ProgramTest
        .testIndexOutOfBoundsException("a = sublist ([10,4,1,2], -1);");
    }
    
    @Test
    public void charTest() {
        ProgramTest.test("a = \"Hi\";", Arrays.asList(new ConstantNode(
                new APValueChar('H')), new ConstantNode(new APValueChar('i'))),
                "a");
        ProgramTest.test("a = ['H', 'i'];", Arrays.asList(new ConstantNode(
                new APValueChar('H')), new ConstantNode(new APValueChar('i'))),
                "a");
    }
    
    @Test
    public void charTestUnicode() {
        ProgramTest.test("a = \"Hi\";", Arrays.asList(new ConstantNode(
                new APValueChar('H')), new ConstantNode(new APValueChar('i'))),
                "a");
        ProgramTest.test("a = ['\\u0048', '\\u0069'];", Arrays.asList(
                new ConstantNode(new APValueChar('H')), new ConstantNode(
                        new APValueChar('i'))), "a");
    }

    @Test
    public void stringConcatCharInList() {
        ProgramTest.test("a = \"Hi\" + ['c'];", Arrays.asList(new ConstantNode(
                new APValueChar('H')), new ConstantNode(new APValueChar('i')),
                new ConstantNode(new APValueChar('c'))), "a");
    }
    
    @Test
    public void stringTest() {
        ProgramTest.test("a = \"Hi\";", Arrays.asList(new ConstantNode(
                new APValueChar('H')), new ConstantNode(new APValueChar('i'))),
                "a");
    }
    
    @Test
    public void stringTestConcat() {
        ProgramTest.test("a = \"H\" + \"i\";", Arrays.asList(new ConstantNode(
                new APValueChar('H')), new ConstantNode(new APValueChar('i'))),
                "a");
    }

    @Test
    public void charTestAddNum() {
        ProgramTest.expectOutput("println(\"H\"{0} + 1);", "I");
    }

    @Test
    public void stringTestMultiplyNegative() {
        ProgramTest.test("a = \"Hi\" * -1;", Arrays.asList(new ConstantNode(
                new APValueChar('i')), new ConstantNode(new APValueChar('H'))),
                "a");
    }

    @Test
    public void multiplyTestOne() {
        ProgramTest.test("a = [1,2] * 1;", ONE_TWO_LIST, "a");
    }

    @Test
    public void multiplyTestTwo() {
        ProgramTest.test("a = [1,2] * 2;", Arrays.asList(new ConstantNode(
                new APValueNum(new BigDecimal("1"))), new ConstantNode(
                        new APValueNum(new BigDecimal("2"))), new ConstantNode(
                                new APValueNum(new BigDecimal("1"))), new ConstantNode(
                                        new APValueNum(new BigDecimal("2")))), "a");
    }
    
    @Test
    public void multiplyTestZero() {
        ProgramTest.test("a = [1,2] * 0;", Arrays.asList(), "a");
    }

    @Test
    public void multiplyTestDecimal() {
        ProgramTest.test("a = [1,2] * 1.5;", Arrays.asList(new ConstantNode(
                new APValueNum(new BigDecimal("1"))), new ConstantNode(
                        new APValueNum(new BigDecimal("2"))), new ConstantNode(
                                new APValueNum(new BigDecimal("1")))), "a");
    }

    @Test
    public void multiplyTestDecimal2() {
        ProgramTest.test("a = [1,2,3,4] * 1.25;", Arrays.asList(
                new ConstantNode(new APValueNum(new BigDecimal("1"))),
                new ConstantNode(new APValueNum(new BigDecimal("2"))),
                new ConstantNode(new APValueNum(new BigDecimal("3"))),
                new ConstantNode(new APValueNum(new BigDecimal("4"))),
                new ConstantNode(new APValueNum(new BigDecimal("1")))), "a");
    }

    @Test
    public void multiplyTestNegativeDecimal() {
        ProgramTest.test("a = [1,2] * -1.5;", Arrays.asList(new ConstantNode(
                new APValueNum(new BigDecimal("1"))), new ConstantNode(
                        new APValueNum(new BigDecimal("2"))), new ConstantNode(
                                new APValueNum(new BigDecimal("1")))), "a");
    }

    @Test
    public void multiplyTestNegativeDecimal2() {
        // Should reverse
        ProgramTest.test("a = [1,2,3,4] * -1.25;", Arrays.asList(
                new ConstantNode(new APValueNum(new BigDecimal("1"))),
                new ConstantNode(new APValueNum(new BigDecimal("4"))),
                new ConstantNode(new APValueNum(new BigDecimal("3"))),
                new ConstantNode(new APValueNum(new BigDecimal("2"))),
                new ConstantNode(new APValueNum(new BigDecimal("1")))), "a");
    }
}
