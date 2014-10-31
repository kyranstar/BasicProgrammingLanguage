/*
 * @author Kyran Adams
 */
package type;

import java.math.BigDecimal;

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
        public APValue callMethod(final Methods s, final APValue arg) {

            throw new MismatchedMethodException("Can't call method " + s
                    + " on type void!");
        }
    };

    /** The value. */
    private T value;

    /**
     * The Class APValueNum.
     */
    public static class APValueNum extends APValue<BigDecimal> {

        /** The Constant TYPE. */
        private static final Class<BigDecimal> TYPE = BigDecimal.class;

        /**
         * Instantiates a new AP value num.
         *
         * @param expressionNode
         *            the expression node
         */
        public APValueNum(final BigDecimal expressionNode) {
            this.setValue(expressionNode);
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "APValueNum<" + getValue() + ">";
        }

        /*
         * (non-Javadoc)
         *
         * @see type.APValue#getType()
         */
        @Override
        public Class<BigDecimal> getType() {
            return TYPE;
        }

        /*
         * (non-Javadoc)
         *
         * @see type.APValue#callMethod(type.APValue.Methods, type.APValue)
         */
        @Override
        public APValue callMethod(final Methods s, final APValue arg) {
            if (!arg.getType().equals(TYPE)) {
                throw new MismatchedMethodException(s
                        + " must take two numerical types. Was " + TYPE
                        + " and " + arg.getType());
            }

            switch (s) {
                case MULTIPLY:
                    return new APValueNum(getValue().multiply(
                            (BigDecimal) arg.getValue()));
                case DIVIDE:
                    return new APValueNum(getValue().divide(
                            (BigDecimal) arg.getValue()));
                case ADD:
                    return new APValueNum(getValue().add(
                            (BigDecimal) arg.getValue()));
                case SUBTRACT:
                    return new APValueNum(getValue().subtract(
                            (BigDecimal) arg.getValue()));
                case POWER:
                    return new APValueNum(getValue().pow(
                            ((BigDecimal) arg.getValue()).intValue()));
            }
            throw new MismatchedMethodException("Can't call method " + s
                    + " on type " + TYPE + " and " + arg.getType());
        }
    }

    /**
     * The Class APValueBool.
     */
    public static class APValueBool extends APValue<Boolean> {

        /** The Constant TYPE. */
        private static final Class<Boolean> TYPE = Boolean.class;

        /**
         * Instantiates a new AP value bool.
         *
         * @param expressionNode
         *            the expression node
         */
        public APValueBool(final Boolean expressionNode) {
            this.setValue(expressionNode);
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
         * @see type.APValue#getType()
         */
        @Override
        public Class<Boolean> getType() {
            return TYPE;
        }

        /*
         * (non-Javadoc)
         *
         * @see type.APValue#callMethod(type.APValue.Methods, type.APValue)
         */
        @Override
        public APValue callMethod(final Methods method, final APValue arg) {
            if (!arg.getType().equals(TYPE)) {
                throw new MismatchedMethodException(method
                        + " must take two bool types. Was " + TYPE + " and "
                        + arg.getType());
            }

            switch (method) {
                case AND:
                    return new APValueBool(getValue()
                            && ((APValueBool) arg).getValue());
                case OR:
                    return new APValueBool(getValue()
                            || ((APValueBool) arg).getValue());
            }
            throw new MismatchedMethodException("Can't call method " + method
                    + " on type bool!");
        }
    }

    /**
     * The Enum Methods.
     */
    public static enum Methods {
        // Number operators
        /** The multiply. */
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
        return value;
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
    public abstract APValue callMethod(Methods s, APValue arg);

    /**
     * Sets the value.
     *
     * @param value
     *            the new value
     */
    public void setValue(final T value) {
        this.value = value;
    }
}
