/**
 * This class has been generated by Fast Code Eclipse Plugin
 * For more information please go to http://fast-code.sourceforge.net/
 * @author : s-KADAMS
 * Created : 02/20/2015 10:06:58
 */

package parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import lexer.Lexer;
import machine.Context;

import org.junit.Test;

import parser.ExpressionNode.AssignmentNode;
import parser.ExpressionNode.ConstantNode;
import parser.ExpressionNode.IndexAssignmentNode;
import parser.ExpressionNode.VariableNode;
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
        final Parser parser = new Parser(new Lexer("= 5;").lex());

        final Context context = getEmptyContext();
        final VariableNode expr = new VariableNode("a");

        // call private method assignment
        final AssignmentNode result = (AssignmentNode) callMethod(parser,
                "assignment", new Class[] { Context.class, VariableNode.class,
                boolean.class }, context, expr, false);

        assertNotNull("result cannot be null", result);
        assertEquals("a", result.getVariable().getName());
    }

    /**
     *
     * @throws Exception
     * @see parser.Parser#indexAssignment(Context,ExpressionNode)
     */
    @Test
    public void indexAssignment() throws Exception {
        final Parser parser = new Parser(new Lexer("{0} = 5;").lex());

        final Context context = getEmptyContext();
        final VariableNode expressionNode = new VariableNode("a");
        // call indexAssignment
        final IndexAssignmentNode result = (IndexAssignmentNode) callMethod(
                parser, "indexAssignment", new Class[] { Context.class,
                        ExpressionNode.class }, context, expressionNode);

        assertNotNull("result cannot be null", result);
        assertEquals("a", ((VariableNode) result.getLeftHand()).getName());
        assertEquals(new ConstantNode<APNumber>(new APValueNum(APNumber.ONE)),
                result.getInsideCurlies());
        assertEquals(
                new ConstantNode<APNumber>(new APValueNum(new APNumber(5))),
                result.getRightHand());
    }
    
    private Object callMethod(final Object object, final String name,
            final Class<Object>[] argTypes, final Object... args)
            throws Exception {

        final Method m = object.getClass().getDeclaredMethod(name, argTypes);

        m.setAccessible(true);

        return m.invoke(object, args);
    }
    
    private static Context getEmptyContext() {
        final OutputStream nullOutputStream = new OutputStream() {
            @Override
            public void write(final int b) throws IOException {

            }
        };
        try {
            return new Context(
                    new PrintStream(nullOutputStream, false, "UTF-8"));
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(
                "I don't really expect this to fail so this is weird.");
    }

}
