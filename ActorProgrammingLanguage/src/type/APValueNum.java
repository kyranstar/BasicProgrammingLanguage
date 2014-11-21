/*
 * @author Kyran Adams
 */
package type;

import java.math.RoundingMode;

import math.APNumber;

/**
 * The Class APValueNum. Represents a real number.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class APValueNum extends APValue<APNumber> {

    /**
     * Instantiates a new AP value num.
     *
     * @param expressionNode
     *            the expression node
     */
    public APValueNum(final APNumber expressionNode) {
        setValue(expressionNode);
    }

    /** The number of decimals to round to if a repeating decimal occurs (10/3). */
    public static final int DECIMALS = 50;
    
    /*
     * (non-Javadoc)
     *
     * @see type.APValue#callMethod(type.APValue.Methods, type.APValue)
     */
    /**
     * Method callMethod.
     *
     * @param s
     *            Operators
     * @param arg
     *            APValue
     * @return APValue
     */
    @Override
    public APValue callMethod(final Operators s, final APValue arg) {
        switch (s) {
            case MULTIPLY:
                if (arg instanceof APValueNum) {
                    return new APValueNum(getValue().multiply(
                            (APNumber) arg.getValue()));
                } else if (arg instanceof APValueList) {
                    return arg.callMethod(Operators.MULTIPLY, this);
                }
                break;
            case DIVIDE:
                if (arg instanceof APValueNum) {
                    return new APValueNum(getValue().divide(
                            (APNumber) arg.getValue(), DECIMALS,
                            RoundingMode.HALF_UP));
                }
                break;
            case MOD:
                if (arg instanceof APValueNum) {
                    return new APValueNum(getValue().remainder(
                            (APNumber) arg.getValue()));
                }
                break;
            case ADD:
                if (arg instanceof APValueNum) {
                    return new APValueNum(getValue().add(
                            (APNumber) arg.getValue()));
                } else if (arg instanceof APValueChar) {
                    return arg.callMethod(Operators.ADD, this);
                }
                break;
            case SUBTRACT:
                if (arg instanceof APValueNum) {
                    return new APValueNum(getValue().subtract(
                            (APNumber) arg.getValue()));
                } else if (arg instanceof APValueChar) {
                    return new APValueChar(
                            (char) (getValue().intValueExact() - (Character) arg
                                    .getValue()));
                }
                break;
            case POWER:
                if (arg instanceof APValueNum) {
                    final APNumber y = (APNumber) arg.getValue();
                    if (y.compareTo(APNumber.MAX_INT_VALUE) < 0
                            && y.compareTo(APNumber.ZERO) > 0 && y.isInteger()) {
                        return new APValueNum(getValue().pow(y.intValueExact()));
                    }
                    return new APValueNum(getValue().pow(
                            (APNumber) arg.getValue()));
                }
                break;
            case EQUAL:
                if (arg instanceof APValueNum) {
                    return new APValueBool(getValue().compareTo(
                            (APNumber) arg.getValue()) == 0);
                }
                break;
            case GREATER:
                if (arg instanceof APValueNum) {
                    return new APValueBool(getValue().compareTo(
                            (APNumber) arg.getValue()) > 0);
                }
                break;
            case GREATER_EQUAL:
                if (arg instanceof APValueNum) {
                    return new APValueBool(getValue().compareTo(
                            (APNumber) arg.getValue()) >= 0);
                }
                break;
            case LESS:
                if (arg instanceof APValueNum) {
                    return new APValueBool(getValue().compareTo(
                            (APNumber) arg.getValue()) < 0);
                }
                break;
            case LESS_EQUAL:
                if (arg instanceof APValueNum) {
                    return new APValueBool(getValue().compareTo(
                            (APNumber) arg.getValue()) <= 0);
                }
                break;
        }

        throw new MismatchedMethodException("Can't call method " + s
                + " on type " + getClass() + " and " + arg.getClass());
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see type.APValue#toString()
     */
    @Override
    public String toString() {
        return getValue().toString();
    }

}
