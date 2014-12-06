/*
 *
 */
package type;

import java.math.RoundingMode;

/**
 * The Class APNumber. Represents an immutable number in the language.
 */
public class APNumber implements Comparable<APNumber> {

    /** The Constant ZERO. */
    public static final APNumber ZERO = new APNumber("0");

    /** The value this number holds. */
    private final double d;
    /** The Constant MAX_INT_VALUE. */
    public static final APNumber MAX_INT_VALUE = new APNumber(Integer.MAX_VALUE);

    /** The Constant ONE. */
    public static final APNumber ONE = new APNumber("1");

    /**
     * Instantiates a new AP number.
     *
     * @param s
     *            the s
     */
    public APNumber(final String s) {
        d = Double.parseDouble(s);
    }
    
    /**
     * Instantiates a new AP number.
     *
     * @param e
     *            the e
     */
    public APNumber(final double e) {
        d = e;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final APNumber o) {
        return ((Double) d).compareTo(o.d);
    }
    
    /**
     * Checks if is integer.
     *
     * @return true, if is integer
     */
    public boolean isInteger() {
        return Math.abs((int) d - d - 1.0) <= 0.000001;
        // return bd.signum() == 0 || bd.scale() <= 0
        // || bd.stripTrailingZeros().scale() <= 0;
    }

    /**
     * Remainder.
     *
     * @param value
     *            the value
     * @return the AP number
     */
    public APNumber remainder(final APNumber value) {
        return new APNumber(d % value.d);
    }

    /**
     * Divide.
     *
     * @param value
     *            the value
     * @param decimals
     *            the decimals
     * @param halfUp
     *            the half up
     * @return the AP number
     */
    public APNumber divide(final APNumber value, final int decimals,
            final RoundingMode halfUp) {
        return new APNumber(d / value.d);
    }

    /**
     * Subtract.
     *
     * @param value
     *            the value
     * @return the AP number
     */
    public APNumber subtract(final APNumber value) {
        return new APNumber(d - value.d);
    }

    /**
     * Adds the.
     *
     * @param value
     *            the value
     * @return the AP number
     */
    public APNumber add(final APNumber value) {
        return new APNumber(d + value.d);
    }

    /**
     * Multiply.
     *
     * @param value
     *            the value
     * @return the AP number
     */
    public APNumber multiply(final APNumber value) {
        return new APNumber(d * value.d);
    }

    /**
     * Int value exact.
     *
     * @return the int
     */
    public int intValueExact() {
        return (int) d;
    }

    /**
     * Pow.
     *
     * @param rightHand
     *            the i
     * @return the AP number
     */
    public APNumber pow(final int rightHand) {
        return new APNumber(Math.pow(d, rightHand));
    }

    /**
     * Pow.
     *
     * @param value
     *            the value
     * @return the AP number
     */
    public APNumber pow(final APNumber value) {
        return new APNumber(Math.pow(d, value.d));
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(d);
        result = prime * result + (int) (temp ^ temp >>> 32);
        return result;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final APNumber other = (APNumber) obj;
        if (Double.doubleToLongBits(d) != Double.doubleToLongBits(other.d)) {
            return false;
        }
        return true;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return removeTrailingZeros(d);
    }
    
    /**
     * Removes the trailing zeros.
     *
     * @param d
     *            the d
     * @return the string
     */
    private static String removeTrailingZeros(final double d) {
        return String.valueOf(d).replaceAll("[0]*$", "").replaceAll(".$", "");
    }

    /**
     * Tan.
     *
     * @return the AP number
     */
    public APNumber tan() {
        return new APNumber(Math.tan(d));
    }

    /**
     * Sqrt.
     *
     * @return the AP number
     */
    public APNumber sqrt() {
        return new APNumber(Math.sqrt(d));
    }

    /**
     * Sin.
     *
     * @return the AP number
     */
    public APNumber sin() {
        return new APNumber(Math.sin(d));
    }

    /**
     * Cos.
     *
     * @return the AP number
     */
    public APNumber cos() {
        return new APNumber(Math.cos(d));
    }

    /**
     * Double value.
     *
     * @return the double
     */
    public double doubleValue() {
        return d;
    }

    /**
     * Negate.
     *
     * @return the AP number
     */
    public APNumber negate() {
        return new APNumber(-d);
    }
    
}
