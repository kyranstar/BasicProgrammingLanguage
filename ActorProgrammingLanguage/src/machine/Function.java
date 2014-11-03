/*
 * @author Kyran Adams
 */
package machine;

import java.util.List;

import parser.ExpressionNode;
import parser.ExpressionNode.VariableNode;

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
    public final FunctionSignature signature;
    
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
        signature = new FunctionSignature(name2, parameters.size());
        this.parameters = parameters;
        this.body = body;
    }
}
