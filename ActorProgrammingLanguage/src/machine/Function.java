/*
 * @author Kyran Adams
 */
package machine;

import java.util.List;

import parser.ExpressionNode;
import parser.ExpressionNode.OrNode.VariableNode;

// TODO: Auto-generated Javadoc
/**
 * The Class Function.
 */
public class Function {

    /** The body. */
    public final ExpressionNode body;

    /** The parameters. */
    public final List<VariableNode> parameters;

    /** The name. */
    public final String name;

    /**
     * Instantiates a new function.
     *
     * @param name2
     *            the name2
     * @param parameters
     *            the parameters
     * @param body
     *            the body
     */
    public Function(final String name2, final List<VariableNode> parameters,
            final ExpressionNode body) {
        name = name2;
        this.parameters = parameters;
        this.body = body;
    }
}
