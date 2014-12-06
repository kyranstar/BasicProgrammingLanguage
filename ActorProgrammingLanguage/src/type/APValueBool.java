/*
 * @author Kyran Adams
 */
package type;

// TODO: Auto-generated Javadoc
/**
 * The Class APValueBool. Represents a boolean value: true or false.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class APValueBool extends APValue<Boolean> {
    
    /**
     * Instantiates a new AP value bool.
     *
     * @param expressionNode
     *            the expression node
     */
    public APValueBool(final Boolean expressionNode) {
        setValue(expressionNode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see type.APValue#callMethod(type.APValue.Methods, type.APValue)
     */
    /**
     * Method callMethod.
     *
     * @param method
     *            Operators
     * @param arg
     *            APValue
     * @return APValue
     */
    @Override
    public APValue callMethod(final Operators method, final APValue arg) {
        
        switch (method) {
            case AND:
                if (arg instanceof APValueBool) {
                    return new APValueBool(getValue()
                            && ((APValueBool) arg).getValue());
                }
            case OR:
                if (arg instanceof APValueBool) {
                    return new APValueBool(getValue()
                            || ((APValueBool) arg).getValue());
                }
            case EQUAL:
                if (arg instanceof APValueBool) {
                    return new APValueBool(
                            getValue() == ((APValueBool) arg).getValue());
                }
        }

        throw new MismatchedMethodException("Can't call method " + method
                + " on type bool!");
    }

    /* (non-Javadoc)
     * @see type.APValue#getType()
     */
    @Override
    public String getType() {
        return "Bool";
    }
}
