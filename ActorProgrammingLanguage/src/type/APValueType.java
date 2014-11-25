package type;

public class APValueType extends APValue{

    private String text;
    public APValueType(String text){
        this.text = text;
    }
    
    @Override
    public APValue callMethod(Operators s, APValue arg) {
        // TODO Auto-generated method stub
        return null;
    }
    public boolean valueIsType(APValue value){
        return value.getType().equals(text);
    }

    @Override
    public String getType() {
        return text;
    }
    
}
