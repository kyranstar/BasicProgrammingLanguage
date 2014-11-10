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
                if (arg.getClass() == APValueNum.class) {
                    return new APValueChar(
                            (char) (getValue() + ((BigDecimal) arg.getValue())
                                    .intValue()));
                } else {
                    break;
                }
        }
        throw new MismatchedMethodException("Can't call method " + s
                + " on type char with param " + arg.getClass().getSimpleName());
    }
}
