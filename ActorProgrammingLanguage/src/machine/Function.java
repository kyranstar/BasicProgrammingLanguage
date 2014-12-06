/*
 * @author Kyran Adams
 */
package machine;

import java.util.List;

import parser.ExpressionNode;
import parser.ExpressionNode.VariableNode;

// TODO: Auto-generated Javadoc
/**
 * The Class Function represents a function in the language.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class Function {
    
    /** The body of the function. */
    public final ExpressionNode body;
    
    /** The parameter names in VariableNode form. */
    public final List<VariableNode> parameters;
    
    /** The name of the function. */
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
    
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return (name != null ? name : "lambda") + "(" + parameters + ") = "
                + body;
    }

}
