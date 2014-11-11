package type;

import java.math.BigDecimal;

public class APValueChar extends APValue<Character> {
    
    public APValueChar(final Character c) {
        setValue(c);
    }
    
    @Override
    public APValue callMethod(final Operators s, final APValue arg) {
        switch (s) {
            case ADD:
                if (arg instanceof APValueNum) {
                    return new APValueChar(
                            (char) (getValue() + ((BigDecimal) arg.getValue())
                                    .intValue()));
                } else if (arg instanceof APValueChar) {
                    return new APValueChar(
                            (char) (getValue() + (Character) arg.getValue()));
                }
                break;
            case SUBTRACT:
                if (arg instanceof APValueNum) {
                    return new APValueChar(
                            (char) (getValue() - ((BigDecimal) arg.getValue())
                                    .intValue()));
                } else if (arg instanceof APValueChar) {
                    return new APValueChar(
                            (char) (getValue() - (Character) arg.getValue()));
                }
                break;
            case EQUAL:
                return new APValueBool(getValue().equals(arg.getValue()));
            case GREATER:
                if (arg instanceof APValueChar) {
                    return new APValueBool(getValue().compareTo(
                            (Character) arg.getValue()) > 0);
                }
                break;
            case GREATER_EQUAL:
                if (arg instanceof APValueChar) {
                    return new APValueBool(getValue().compareTo(
                            (Character) arg.getValue()) >= 0);
                }
                break;
            case LESS:
                if (arg instanceof APValueChar) {
                    return new APValueBool(getValue().compareTo(
                            (Character) arg.getValue()) < 0);
                }
                break;
            case LESS_EQUAL:
                if (arg instanceof APValueChar) {
                    return new APValueBool(getValue().compareTo(
                            (Character) arg.getValue()) <= 0);
                }
                break;
        }
        throw new MismatchedMethodException("Can't call method " + s
                + " on type char with param " + arg.getClass().getSimpleName());
    }
}
