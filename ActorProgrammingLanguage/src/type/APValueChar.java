package type;

import java.math.BigDecimal;

public class APValueChar extends APValue<Character> {

    public APValueChar(final Character c) {
        setValue(c);
    }
    
    @Override
    public String toString() {
        return "APValueChar<" + getValue() + ">";
    }
    
    @Override
    public Class getType() {
        return Character.class;
    }

    @Override
    public APValue callMethod(final Operators s, final APValue arg) {
        switch (s) {
            case ADD:
                if (arg.getType() == BigDecimal.class) {
                    return new APValueChar(
                            (char) (getValue() + ((BigDecimal) arg.getValue())
                                    .intValue()));
                } else {
                    break;
                }
        }
        throw new MismatchedMethodException("Can't call method " + s
                + " on type char with param " + arg.getType().getSimpleName());
    }
}
