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
    
    /** The Constant LIST_ONE_THROUGH_FIVE. */
    private static final List<ConstantNode> LIST_ONE_THROUGH_FIVE = Arrays
            .asList(new ConstantNode(new APValueNum(new BigDecimal("1"))),
                    new ConstantNode(new APValueNum(new BigDecimal("2"))),
                    new ConstantNode(new APValueNum(new BigDecimal("3"))),
                    new ConstantNode(new APValueNum(new BigDecimal("4"))),
                    new ConstantNode(new APValueNum(new BigDecimal("5"))));

    /**
     * Index operator and concat.
     */
    @Test
    public void indexOperatorAndConcat() {
        ProgramTest.test("a = ([1] + [[2]{0}]){0};", new BigDecimal("1"), "a");
    }

    /**
     * Concat and index.
     */
    @Test
    public void concatAndIndex() {
        ProgramTest.test("a = ([1] + [2]){0};", new BigDecimal("1"), "a");
    }

    /**
     * Index operator2.
     */
    @Test
    public void indexOperator2() {
        ProgramTest.test("a = [1,2]{0};", new BigDecimal("1"), "a");
    }

    /**
     * List function returning parameter.
     */
    @Test
    public void listFunctionReturningParameter() {
        ProgramTest.test("f a = [a,a,a]; c = f(3){1};", new BigDecimal("3"),
                "c");
    }

    /**
     * Index operator.
     */
    @Test
    public void indexOperator() {
        ProgramTest.test("b = 2; c = [1] + [b]; a = c{1};",
                new BigDecimal("2"), "a");
    }

    /**
     * List and range.
     */
    @Test
    public void listAndRange() {
        ProgramTest.test(
                "a = [1,2,3] + (4 to 6) + [7,8,9] = [1,2,3,4,5,6,7,8,9];",
                true, "a");
    }

    /**
     * Variable list.
     */
    @Test
    public void variableList() {
        ProgramTest.test("b = 2; a = [1] + [b];", Arrays.asList(
                new ConstantNode(new APValueNum(new BigDecimal("1"))),
                new VariableNode("b")), "a");
    }

    @Test
    public void emptyList() {
        ProgramTest.test("mut a = []; a = a + [1,2];", ONE_TWO_LIST, "a");
    }
    
    /**
     * List concat.
     */
    @Test
    public void listConcat() {
        ProgramTest.test("a = [1] + [2];", ONE_TWO_LIST, "a");
    }

    /**
     * List declaration.
     */
    @Test
    public void listDeclaration() {
        ProgramTest.test("a = [1,2];", ONE_TWO_LIST, "a");
    }

    /**
     * Sublist double param2.
     */
    @Test
    public void sublistDoubleParam2() {
        ProgramTest.test("a = sublist ([10,4,1,2], 1, 3);", Arrays.asList(
                new ConstantNode(new APValueNum(new BigDecimal("4"))),
                new ConstantNode(new APValueNum(new BigDecimal("1")))), "a");
    }

    /**
     * Sublist double param.
     */
    @Test
    public void sublistDoubleParam() {
        ProgramTest.test("a = sublist ([10,4,1,2], 2, 4);", ONE_TWO_LIST, "a");
    }

    /**
     * Char test.
     */
    @Test
    public void charTest() {
        ProgramTest.test("a = \"Hi\";", Arrays.asList(new ConstantNode(
                new APValueChar('H')), new ConstantNode(new APValueChar('i'))),
                "a");
        ProgramTest.test("a = ['H', 'i'];", Arrays.asList(new ConstantNode(
                new APValueChar('H')), new ConstantNode(new APValueChar('i'))),
                "a");
    }
    
    /**
     * Char test unicode.
     */
    @Test
    public void charTestUnicode() {
        ProgramTest.test("a = \"Hi\";", Arrays.asList(new ConstantNode(
                new APValueChar('H')), new ConstantNode(new APValueChar('i'))),
                "a");
        ProgramTest.test("a = ['\\u0048', '\\u0069'];", Arrays.asList(
                new ConstantNode(new APValueChar('H')), new ConstantNode(
                        new APValueChar('i'))), "a");
    }

    /**
     * String concat char in list.
     */
    @Test
    public void stringConcatCharInList() {
        ProgramTest.test("a = \"Hi\" + ['c'];", Arrays.asList(new ConstantNode(
                new APValueChar('H')), new ConstantNode(new APValueChar('i')),
                new ConstantNode(new APValueChar('c'))), "a");
    }
    
    /**
     * String test.
     */
    @Test
    public void stringTest() {
        ProgramTest.test("a = \"Hi\";", Arrays.asList(new ConstantNode(
                new APValueChar('H')), new ConstantNode(new APValueChar('i'))),
                "a");
    }
    
    /**
     * String test concat.
     */
    @Test
    public void stringTestConcat() {
        ProgramTest.test("a = \"H\" + \"i\";", Arrays.asList(new ConstantNode(
                new APValueChar('H')), new ConstantNode(new APValueChar('i'))),
                "a");
    }

    /**
     * Char test add num.
     */
    @Test
    public void charTestAddNum() {
        ProgramTest.expectOutput("println(\"H\"{0} + 1);", "I");
    }

    /**
     * String test multiply negative.
     */
    @Test
    public void stringTestMultiplyNegative() {
        ProgramTest.test("a = \"Hi\" * -1;", Arrays.asList(new ConstantNode(
                new APValueChar('i')), new ConstantNode(new APValueChar('H'))),
                "a");
    }
    
    /**
     * String test multiply backwards.
     */
    @Test
    public void stringTestMultiplyBackwards() {
        ProgramTest.test("a = 1 * \"Hi\";", Arrays.asList(new ConstantNode(
                new APValueChar('H')), new ConstantNode(new APValueChar('i'))),
                "a");
    }
    
    /**
     * Multiply test one.
     */
    @Test
    public void multiplyTestOne() {
        ProgramTest.test("a = [1,2] * 1;", ONE_TWO_LIST, "a");
    }

    /**
     * Multiply test two.
     */
    @Test
    public void multiplyTestTwo() {
        ProgramTest.test("a = [1,2] * 2;", Arrays.asList(new ConstantNode(
                new APValueNum(new BigDecimal("1"))), new ConstantNode(
                new APValueNum(new BigDecimal("2"))), new ConstantNode(
                new APValueNum(new BigDecimal("1"))), new ConstantNode(
                new APValueNum(new BigDecimal("2")))), "a");
    }
    
    /**
     * Multiply test zero.
     */
    @Test
    public void multiplyTestZero() {
        ProgramTest.test("a = [1,2] * 0;", Arrays.asList(), "a");
    }

    /**
     * Multiply test decimal.
     */
    @Test
    public void multiplyTestDecimal() {
        ProgramTest.test("a = [1,2] * 1.5;", Arrays.asList(new ConstantNode(
                new APValueNum(new BigDecimal("1"))), new ConstantNode(
                        new APValueNum(new BigDecimal("2"))), new ConstantNode(
                                new APValueNum(new BigDecimal("1")))), "a");
    }

    /**
     * Multiply test decimal2.
     */
    @Test
    public void multiplyTestDecimal2() {
        ProgramTest.test("a = [1,2,3,4] * 1.25;", Arrays.asList(
                new ConstantNode(new APValueNum(new BigDecimal("1"))),
                new ConstantNode(new APValueNum(new BigDecimal("2"))),
                new ConstantNode(new APValueNum(new BigDecimal("3"))),
                new ConstantNode(new APValueNum(new BigDecimal("4"))),
                new ConstantNode(new APValueNum(new BigDecimal("1")))), "a");
    }

    /**
     * Multiply test negative decimal.
     */
    @Test
    public void multiplyTestNegativeDecimal() {
        ProgramTest.test("a = [1,2] * -1.5;", Arrays.asList(new ConstantNode(
                new APValueNum(new BigDecimal("1"))), new ConstantNode(
                        new APValueNum(new BigDecimal("2"))), new ConstantNode(
                                new APValueNum(new BigDecimal("1")))), "a");
    }

    /**
     * Multiply test negative decimal2.
     */
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
    
    /**
     * Range inclusive2.
     */
    @Test
    public void rangeInclusive2() {
        ProgramTest.test("c = [1,2,3,4,5] = 1 to 5;", true, "c");
    }

    /**
     * Range inclusive.
     */
    @Test
    public void rangeInclusive() {
        ProgramTest.test("c = [1,2,3,4,5];", LIST_ONE_THROUGH_FIVE, "c");
        ProgramTest.test("c = 1 to 5;", LIST_ONE_THROUGH_FIVE, "c");
    }
    
    /**
     * Range get.
     */
    @Test
    public void rangeGet() {
        ProgramTest.test("c = (1 to 5){0};", new BigDecimal("1"), "c");
    }

    /**
     * Range sublist.
     */
    @Test
    public void rangeSublist() {
        ProgramTest.test("c = sublist(1 to 5,0,2);", ONE_TWO_LIST, "c");
    }
}
