/**
 * This class has been generated by Fast Code Eclipse Plugin
 * For more information please go to http://fast-code.sourceforge.net/
 * @author : s-KADAMS
 * Created : 02/20/2015 10:06:58
 */

package parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import lexer.Lexer;
import machine.Context;

import org.junit.Test;

import parser.ExpressionNode.AdditionNode;
import parser.ExpressionNode.AssignmentNode;
import parser.ExpressionNode.ConstantNode;
import parser.ExpressionNode.IndexAssignmentNode;
import parser.ExpressionNode.SequenceNode;
import parser.ExpressionNode.VariableNode;
import total.ProgramTest;
import type.APNumber;
import type.APValueNum;

public class ParserTest {

    /**
     *
     * @throws Exception
     * @see parser.Parser#assignment(Context,VariableNode,boolean)
     */
    @Test
    public void assignment() throws Exception {

        final Map<String, ExpressionNode> toTest = new HashMap<>();
        toTest.put("= 5", new AssignmentNode(new VariableNode("a"),
                new ConstantNode<APNumber>(new APValueNum(new APNumber(5))),
                false));
        toTest.put("= x", new AssignmentNode(new VariableNode("a"),
                new VariableNode("x"), false));
        
        toTest.put("= 5+1", new AssignmentNode(new VariableNode("a"),
                new AdditionNode(new ConstantNode<APNumber>(new APValueNum(
                        new APNumber(5))), new ConstantNode<APNumber>(
                        new APValueNum(new APNumber(1)))), false));

        final Context context = ProgramTest.getEmptyContext();
        final VariableNode expr = new VariableNode("a");

        testMethod(
                toTest,
                t -> {
                    assertEquals("a", ((AssignmentNode) t.getValue())
                            .getVariable().getName());

                }, "assignment", new Class[] { Context.class,
                        VariableNode.class, boolean.class }, context, expr,
                        false);

    }
    
    /**
     *
     * @throws Exception
     * @see parser.Parser#assignment(Context,VariableNode,boolean)
     */
    @Test
    public void seqExpr() throws Exception {

        final Map<String, ExpressionNode> toTest = new HashMap<>();
        toTest.put(
                "{x = 1; y = 2; return b;}",
                new SequenceNode(Arrays.asList(new AssignmentNode(
                        new VariableNode("x"), new ConstantNode<APNumber>(
                                new APValueNum(new APNumber(1))), false),
                        new AssignmentNode(new VariableNode("y"),
                                new ConstantNode<APNumber>(new APValueNum(
                                        new APNumber(2))), false)),
                        new VariableNode("b")));

        final Context context = ProgramTest.getEmptyContext();

        testMethod(toTest, t -> {
            System.out.println(t.getValue());

        }, "seqExpr", new Class[] { Context.class }, context);

    }
    
    /**
     *
     * @throws Exception
     * @see parser.Parser#indexAssignment(Context,ExpressionNode)
     */
    @Test
    public void indexAssignment() throws Exception {
        final Map<String, ExpressionNode> toTest = new HashMap<>();

        toTest.put("{0} = 5", new IndexAssignmentNode(new VariableNode("a"),
                new ConstantNode<APNumber>(new APValueNum(new APNumber(0))),
                new ConstantNode<APNumber>(new APValueNum(new APNumber(5)))));
        toTest.put("{x} = y", new IndexAssignmentNode(new VariableNode("a"),
                new VariableNode("x"), new VariableNode("y")));
        
        final Context context = ProgramTest.getEmptyContext();
        final VariableNode expr = new VariableNode("a");

        testMethod(
                toTest,
                t -> {
                    assertEquals("a", ((VariableNode) ((IndexAssignmentNode) t
                            .getValue()).getLeftHand()).getName());
                }, "indexAssignment", new Class[] { Context.class,
                        ExpressionNode.class }, context, expr);
    }

    /**
     * This method tests a method from the Parser class by looping through each
     * testCase key and asserting that the result is both not null and equal to
     * the testCase result. It also calls the consumer with the entry of the key
     * and the result so that the caller can run their own tests.
     *
     * @param testCases
     *            String is the parameter to the parser, ExpressionNode is the
     *            expected result
     * @param tests
     *            Additional tests the user might like to run on the parameter
     *            and the result
     * @param name
     *            The name of the method in the Parser class
     * @param argTypes
     *            The type of arguments that are parameters to the method
     * @param args
     *            The arguments to be passed into the method
     * @throws Exception
     */
    private void testMethod(final Map<String, ExpressionNode> testCases,
            final Consumer<Entry<String, ExpressionNode>> tests,
            final String name, final Class[] argTypes, final Object... args)
                    throws Exception {

        for (final Entry<String, ExpressionNode> e : testCases.entrySet()) {
            final Parser parser = new Parser(new Lexer(e.getKey()).lex());

            // call private method assignment
            final ExpressionNode<?> result = (ExpressionNode<?>) callMethod(
                    parser, name, argTypes, args);

            assertNotNull("result cannot be null", result);
            assertEquals(e.getValue(), result);
            tests.accept(new Entry<String, ExpressionNode>() {

                @Override
                public String getKey() {
                    return e.getKey();
                }

                @Override
                public ExpressionNode getValue() {
                    return result;
                }
                
                @Override
                public ExpressionNode setValue(final ExpressionNode value) {
                    // TODO Auto-generated method stub
                    return null;
                }
            });
        }
    }

    private Object callMethod(final Object object, final String name,
            final Class<Object>[] argTypes, final Object... args)
                    throws Exception {

        final Method m = object.getClass().getDeclaredMethod(name, argTypes);

        m.setAccessible(true);

        return m.invoke(object, args);
    }

}
