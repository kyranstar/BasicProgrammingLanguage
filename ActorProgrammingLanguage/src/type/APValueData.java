/*
 * 
 */
package type;

// TODO: Auto-generated Javadoc
/**
 * The Class APValueData.
 */
public class APValueData extends APValue<DataStructureInstance> {
    /**
     * Instantiates a new AP value data.
     *
     * @param data
     *            the expression node
     */
    public APValueData(final DataStructureInstance data) {
        setValue(data);
    }
    
    /* (non-Javadoc)
     * @see type.APValue#callMethod(type.APValue.Operators, type.APValue)
     */
    @Override
    public APValue callMethod(final Operators s, final APValue arg) {
        throw new MismatchedMethodException("Can't call method " + s
                + " on type data!");
    }

    /* (non-Javadoc)
     * @see type.APValue#getType()
     */
    @Override
    public String getType() {
        return getValue().type;
    }

}
