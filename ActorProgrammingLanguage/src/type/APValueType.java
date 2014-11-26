package type;

public class APValueType extends APValue {

    private final String text;

    public APValueType(final String text) {
        this.text = text;
    }
    
    @Override
    public APValue callMethod(final Operators s, final APValue arg) {
        switch (s) {
            case EQUAL:
                return new APValueBool(equals(arg));
        }

        throw new MismatchedMethodException("Can't call method " + s
                + " on type " + getClass() + " and " + arg.getClass());
    }

    public boolean valueIsType(final APValue value) {
        System.out.println("Value (" + value.getType().split("\\$")[0]
                + ") and (" + text + ") = "
                + value.getType().split("\\$")[0].equals(text));

        return value.getType().equals(text)
                || value.getType().split("\\$")[0].equals(text);
    }

    @Override
    public String getType() {
        return text;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (text == null ? 0 : text.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final APValueType other = (APValueType) obj;
        if (text == null) {
            if (other.text != null) {
                return false;
            }
        } else if (!text.equals(other.text)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return text;
    }
}
