/*
 *
 */
package type;

// TODO: Auto-generated Javadoc
/**
 * The Class APValueType.
 */
public class APValueType extends APValue<String> {
    
    /**
     * Instantiates a new AP value type.
     *
     * @param text
     *            the text
     */
    public APValueType(final String text) {
        setValue(text);
    }

    /*
     * (non-Javadoc)
     *
     * @see type.APValue#callMethod(type.APValue.Operators, type.APValue)
     */
    @Override
    public APValue callMethod(final Operators s, final APValue arg) {
        switch (s) {
            case EQUAL:
                return new APValueBool(equals(arg));
        }
        
        throw new MismatchedMethodException("Can't call method " + s
                + " on type " + getClass() + " and " + arg.getClass());
    }
    
    /**
     * Value is type.
     *
     * @param value
     *            the value
     * @return true, if successful
     */
    public boolean valueIsType(final APValue value) {
        return value.getType().equals(getValue())
                || value.getType().split("\\$")[0].equals(getValue());
    }
    
    /*
     * (non-Javadoc)
     *
     * @see type.APValue#getType()
     */
    @Override
    public String getType() {
        return getValue();
    }
    
    /*
     * (non-Javadoc)
     *
     * @see type.APValue#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + (getValue() == null ? 0 : getValue().hashCode());
        return result;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see type.APValue#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final APValueType other = (APValueType) obj;
        if (getValue() == null) {
            if (other.getValue() != null) {
                return false;
            }
        } else if (!getValue().equals(other.getValue())) {
            return false;
        }
        return true;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see type.APValue#toString()
     */
    @Override
    public String toString() {
        return getValue();
    }
}
