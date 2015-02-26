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
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (body == null ? 0 : body.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result
                + (parameters == null ? 0 : parameters.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Function other = (Function) obj;
        if (body == null) {
            if (other.body != null) {
                return false;
            }
        } else if (!body.equals(other.body)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (parameters == null) {
            if (other.parameters != null) {
                return false;
            }
        } else if (!parameters.equals(other.parameters)) {
            return false;
        }
        return true;
    }
    
}
