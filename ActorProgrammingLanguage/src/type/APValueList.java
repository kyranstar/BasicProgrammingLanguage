package type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class APValueList extends APValue<List> {
    
    /** The Constant TYPE. */
    private static final Class<List> TYPE = List.class;
    
    /**
     * Instantiates a new AP value bool.
     *
     * @param expressionNode
     *            the expression node
     */
    public APValueList(final List<APValue> expressionNode) {
        final List<APValue> b = new ArrayList<>(expressionNode.size());
        Collections.copy(b, expressionNode);
        setValue(Collections.unmodifiableList(b));
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "APValueList<" + getValue() + ">";
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see type.APValue#getType()
     */
    @Override
    public Class<List> getType() {
        return TYPE;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see type.APValue#callMethod(type.APValue.Methods, type.APValue)
     */
    @Override
    public APValue callMethod(final Operators method, final APValue arg) {
        if (!arg.getType().equals(TYPE)) {
            throw new MismatchedMethodException(method
                    + " must take two bool types. Was " + TYPE + " and "
                    + arg.getType());
        }
        
        switch (method) {
            case ADD:
                return new APValueList(
                        append(getValue(), (List) arg.getValue()));
        }
        throw new MismatchedMethodException("Can't call method " + method
                + " on type bool!");
    }

    private List<APValue> append(final List value, final List value2) {
        // TODO Auto-generated method stub
        return null;
    }
}
