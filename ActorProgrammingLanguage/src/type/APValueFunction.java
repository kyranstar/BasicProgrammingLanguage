package type;

import machine.Function;

public class APValueFunction extends APValue<Function> {

    public APValueFunction(final Function function) {
        setValue(function);
    }
    
    @Override
    public APValue callMethod(final type.APValue.Operators s, final APValue arg) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
