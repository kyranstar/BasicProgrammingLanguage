/*
 * @author Kyran Adams
 */
package type;

import java.math.BigDecimal;
import java.math.RoundingMode;

import math.BigDecimalMath;

// TODO: Auto-generated Javadoc
/**
 * The Class APValueNum.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class APValueNum extends APValue<BigDecimal> {
    
    /**
     * Instantiates a new AP value num.
     *
     * @param expressionNode
     *            the expression node
     */
    public APValueNum(final BigDecimal expressionNode) {
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
                            (BigDecimal) arg.getValue()));
                } else if (arg instanceof APValueList) {
                    return arg.callMethod(Operators.MULTIPLY, this);
                }
                break;
            case DIVIDE:
                if (arg instanceof APValueNum) {
                    return new APValueNum(getValue().divide(
                            (BigDecimal) arg.getValue(), DECIMALS,
                            RoundingMode.HALF_UP));
                }
                break;
            case ADD:
                if (arg instanceof APValueNum) {
                    return new APValueNum(getValue().add(
                            (BigDecimal) arg.getValue()));
                } else if (arg instanceof APValueChar) {
                    return arg.callMethod(Operators.ADD, this);
                }
                break;
            case SUBTRACT:
                if (arg instanceof APValueNum) {
                    return new APValueNum(getValue().subtract(
                            (BigDecimal) arg.getValue()));
                }
                break;
            case POWER:
                if (arg instanceof APValueNum) {
                    return new APValueNum(BigDecimalMath.pow(getValue(),
                            (BigDecimal) arg.getValue()));
                }
                break;
            case EQUAL:
                if (arg instanceof APValueNum) {
                    return new APValueBool(getValue().compareTo(
                            (BigDecimal) arg.getValue()) == 0);
                }
                break;
            case GREATER:
                if (arg instanceof APValueNum) {
                    return new APValueBool(getValue().compareTo(
                            (BigDecimal) arg.getValue()) > 0);
                }
                break;
            case GREATER_EQUAL:
                if (arg instanceof APValueNum) {
                    return new APValueBool(getValue().compareTo(
                            (BigDecimal) arg.getValue()) >= 0);
                }
                break;
            case LESS:
                if (arg instanceof APValueNum) {
                    return new APValueBool(getValue().compareTo(
                            (BigDecimal) arg.getValue()) < 0);
                }
                break;
            case LESS_EQUAL:
                if (arg instanceof APValueNum) {
                    return new APValueBool(getValue().compareTo(
                            (BigDecimal) arg.getValue()) <= 0);
                }
                break;
        }
        
        throw new MismatchedMethodException("Can't call method " + s
                + " on type " + getClass() + " and " + arg.getClass());
    }

    @Override
    public String toString() {
        return getValue().stripTrailingZeros().toPlainString();
    }
}
