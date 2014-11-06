package type;


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
            default:
                throw new MismatchedMethodException("Can't call method " + s
                        + " on type string!");
        }
    }
    
}
