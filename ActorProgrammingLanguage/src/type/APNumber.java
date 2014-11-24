package type;

import java.math.RoundingMode;

public class APNumber implements Comparable<APNumber> {
    
    public static final APNumber ZERO = new APNumber("0");
    double d;
    /** The Constant MAX_INT_VALUE. */
    public static final APNumber MAX_INT_VALUE = new APNumber(Integer.MAX_VALUE
            + "");
    public static final APNumber ONE = new APNumber("1");
    
    public APNumber(final String s) {
        d = Double.parseDouble(s);
    }

    public APNumber(final double e) {
        d = e;
    }
    
    @Override
    public int compareTo(final APNumber o) {
        return ((Double) d).compareTo(o.d);
    }

    public boolean isInteger() {
        return Math.abs((int) d - d - 1.0) <= 0.000001;
        // return bd.signum() == 0 || bd.scale() <= 0
        // || bd.stripTrailingZeros().scale() <= 0;
    }
    
    public APNumber remainder(final APNumber value) {
        return new APNumber(d % value.d);
    }
    
    public APNumber divide(final APNumber value, final int decimals,
            final RoundingMode halfUp) {
        return new APNumber(d / value.d);
    }
    
    public APNumber subtract(final APNumber value) {
        return new APNumber(d - value.d);
    }
    
    public APNumber add(final APNumber value) {
        return new APNumber(d + value.d);
    }
    
    public APNumber multiply(final APNumber value) {
        return new APNumber(d * value.d);
    }
    
    public int intValueExact() {
        return (int) d;
    }
    
    public APNumber pow(final int i) {
        return new APNumber(Math.pow(d, i));
    }
    
    public APNumber pow(final APNumber value) {
        return new APNumber(Math.pow(d, value.d));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(d);
        result = prime * result + (int) (temp ^ temp >>> 32);
        return result;
    }

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

    @Override
    public String toString() {
        return removeTrailingZeros(d);
    }

    private static String removeTrailingZeros(final double d) {
        return String.valueOf(d).replaceAll("[0]*$", "").replaceAll(".$", "");
    }
    
    public APNumber tan() {
        return new APNumber(Math.tan(d));
    }
    
    public APNumber sqrt() {
        return new APNumber(Math.sqrt(d));
    }
    
    public APNumber sin() {
        return new APNumber(Math.sin(d));
    }
    
    public APNumber cos() {
        return new APNumber(Math.cos(d));
    }
    
    public double doubleValue() {
        return d;
    }
    
    public APNumber negate() {
        return new APNumber(-d);
    }

}
