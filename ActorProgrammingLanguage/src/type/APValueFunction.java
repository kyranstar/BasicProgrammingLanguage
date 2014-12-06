/*
 * 
 */
package type;

import machine.Function;

// TODO: Auto-generated Javadoc
/**
 * The Class APValueFunction. Represents a first class function.
 */
public class APValueFunction extends APValue<Function> {

    /**
     * Instantiates a new AP value function.
     *
     * @param function
     *            the function
     */
    public APValueFunction(final Function function) {
        setValue(function);
    }

    /*
     * (non-Javadoc)
     * 
     * @see type.APValue#callMethod(type.APValue.Operators, type.APValue)
     */
    @Override
    public APValue callMethod(final type.APValue.Operators s, final APValue arg) {
        throw new MismatchedMethodException("Can't call method " + s
                + " on type char with param " + arg.getClass().getSimpleName());
    }

    /* (non-Javadoc)
     * @see type.APValue#getType()
     */
    @Override
    public String getType() {
        return "Func";
    }
    
}
