/*
 * @author Kyran Adams
 */
package type;

// TODO: Auto-generated Javadoc
/**
 * The Class APValue. Holds a value
 *
 * @param <T>
 *            the generic type this value holds.
 */
public abstract class APValue<T> {

    /** The Constant VOID. */
    public static final APValue<Void> VOID = new APValue<Void>() {
        @Override
        public String toString() {
            return "VOID";
        }

        @Override
        public Class<Void> getType() {
            return Void.class;
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
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public abstract Class<T> getType();

    /**
     * Gets the value.
     *
     * @return the value
     */
    public T getValue() {
        return data;
    }

    /**
     * Call method.
     *
     * @param s
     *            the s
     * @param arg
     *            the arg
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
