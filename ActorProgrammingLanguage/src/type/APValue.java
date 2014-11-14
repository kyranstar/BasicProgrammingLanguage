/*
 * @author Kyran Adams
 */
package type;

/**
 * The Class APValue. Holds a value in the language.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 * @param <T>
 *            the generic type
 */
public abstract class APValue<T> {

    /** The Constant VOID. */
    public static final APValue<Void> VOID = new APValue<Void>() {
        @Override
        public String toString() {
            return "VOID";
        }

        @Override
        public APValue callMethod(final Operators s, final APValue arg) {

            throw new MismatchedMethodException("Can't call method " + s
                    + " on type void!");
        }
    };

    /** The data. */
    private T data;

    /**
     * The Class APValueBool.
     *
     * @author Kyran Adams
     * @version $Revision: 1.0 $
     */

    /**
     * The Enum Methods.
     */
    public static enum Operators {
        // Number operators
        /** The multiply operator. */
        MULTIPLY,

        /** The divide. */
        DIVIDE,

        /** The subtract. */
        SUBTRACT,

        /** The add. */
        ADD,

        /** The power. */
        POWER,
        // Boolean operators
        /** The and. */
        AND,

        /** The or. */
        OR,
        
        /** The greater equal. */
        GREATER_EQUAL,
        
        /** The equal. */
        EQUAL,
        
        /** The less equal. */
        LESS_EQUAL,
        
        /** The greater. */
        GREATER,
        
        /** The less. */
        LESS,

        /** The mod. */
        MOD,
    }

    /**
     * Gets the value.
     *
     *
     * @return the value
     */
    public T getValue() {
        return data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getValue().toString();
    }

    /**
     * Call method.
     *
     * @param s
     *            the s
     * @param arg
     *            the arg
     *
     * @return the AP value
     */
    public abstract APValue callMethod(Operators s, APValue arg);

    /**
     * Sets the value.
     *
     * @param value
     *            the new value
     */
    public void setValue(final T value) {
        this.data = value;
    }
}
