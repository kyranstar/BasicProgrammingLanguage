/*
 * @author Kyran Adams
 */
package type;

import java.math.BigDecimal;
import java.math.RoundingMode;

import parser.math.BigDecimalMath;

// TODO: Auto-generated Javadoc
/**
 * The Class APValueNum.
 */
public class APValueNum extends APValue<BigDecimal> {
    
    /** The Constant TYPE. */
    private static final Class<BigDecimal> TYPE = BigDecimal.class;
    
    /**
     * Instantiates a new AP value num.
     *
     * @param expressionNode
     *            the expression node
     */
    public APValueNum(final BigDecimal expressionNode) {
        setValue(expressionNode);
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

    /** The number of decimals to round to if a repeating decimal occurs (10/3). */
    public static final int DECIMALS = 50;

    /*
     * (non-Javadoc)
     * 
     * @see type.APValue#callMethod(type.APValue.Methods, type.APValue)
     */
    @Override
    public APValue callMethod(final Operators s, final APValue arg) {
        
        if (!arg.getType().equals(TYPE)) {
            throw new MismatchedMethodException(s
                    + " must take two numerical types. Was " + TYPE + " and "
                    + arg.getType());
        }
        
        switch (s) {
            case MULTIPLY:
                return new APValueNum(getValue().multiply(
                        (BigDecimal) arg.getValue()));
            case DIVIDE:
                return new APValueNum(getValue().divide(
                        (BigDecimal) arg.getValue(), DECIMALS,
                        RoundingMode.HALF_UP));
            case ADD:
                return new APValueNum(getValue().add(
                        (BigDecimal) arg.getValue()));
            case SUBTRACT:
                return new APValueNum(getValue().subtract(
                        (BigDecimal) arg.getValue()));
            case POWER:
                return new APValueNum(BigDecimalMath.pow(getValue(),
                        (BigDecimal) arg.getValue()));
            case EQUAL:
                return new APValueBool(getValue().compareTo(
                        (BigDecimal) arg.getValue()) == 0);
            case GREATER:
                return new APValueBool(getValue().compareTo(
                        (BigDecimal) arg.getValue()) > 0);
            case GREATER_EQUAL:
                return new APValueBool(getValue().compareTo(
                        (BigDecimal) arg.getValue()) >= 0);
            case LESS:
                return new APValueBool(getValue().compareTo(
                        (BigDecimal) arg.getValue()) < 0);
            case LESS_EQUAL:
                return new APValueBool(getValue().compareTo(
                        (BigDecimal) arg.getValue()) <= 0);
            default:
                throw new MismatchedMethodException("Can't call method " + s
                        + " on type " + TYPE + " and " + arg.getType());
        }
    }
}
