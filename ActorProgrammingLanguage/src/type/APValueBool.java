/*
 * @author Kyran Adams
 */
package type;

// TODO: Auto-generated Javadoc
/**
 * The Class APValueBool.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class APValueBool extends APValue<Boolean> {

    /** The Constant TYPE. */
    private static final Class<Boolean> TYPE = Boolean.class;

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
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "APValueBool<" + getValue() + ">";
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
                return new APValueBool(getValue()
                        && ((APValueBool) arg).getValue());
            case OR:
                return new APValueBool(getValue()
                        || ((APValueBool) arg).getValue());
            default:
                throw new MismatchedMethodException("Can't call method "
                        + method + " on type bool!");
        }
    }
}
