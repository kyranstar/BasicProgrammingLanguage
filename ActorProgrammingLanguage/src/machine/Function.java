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
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
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
     * @param name
     *            the name
     * @param parameters
     *            the parameters
     * @param body
     *            the body
     */
    public Function(final String name, final List<VariableNode> parameters,
            final ExpressionNode body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }
    
    @Override
    public String toString() {
        return name + "(" + parameters + ") = " + body;
    }

}
