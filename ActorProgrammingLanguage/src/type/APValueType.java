package type;

public class APValueType extends APValue {

    private final String text;

    public APValueType(final String text) {
        this.text = text;
    }
    
    @Override
    public APValue callMethod(final Operators s, final APValue arg) {
        // TODO Auto-generated method stub
        return null;
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
    
}
