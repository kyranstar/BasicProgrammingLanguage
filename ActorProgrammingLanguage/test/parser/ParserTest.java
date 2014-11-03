/*
 * @author Kyran Adams
 */
package parser;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import lexer.Lexer;
import machine.Context;

import org.junit.Test;

import parser.ExpressionNode.AdditionNode;
import parser.ExpressionNode.AssignmentNode;
import parser.ExpressionNode.ConstantNode;
import parser.ExpressionNode.VariableNode;
import type.APValueNum;

// TODO: Auto-generated Javadoc
/**
 * The Class ParserTest.
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class ParserTest {

    /**
     * Test.
     */
    @Test
    public void test() {
        final Context c = new Context(System.out);
        final List<ExpressionNode> tokens = new Parser(
                new Lexer("b = 1+3;").lex()).parse(c);
        final ExpressionNode expected = new AssignmentNode(
                new VariableNode(""), new AdditionNode(new ConstantNode(
                        new APValueNum(new BigDecimal("1"))), new ConstantNode(
                                new APValueNum(new BigDecimal("3")))));
        assertEquals(1, tokens.size());
        assertEquals(expected, tokens.get(0));
    }

}
