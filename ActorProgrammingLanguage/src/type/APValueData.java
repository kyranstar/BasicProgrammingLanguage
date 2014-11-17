package type;

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
    
    @Override
    public APValue callMethod(final Operators s, final APValue arg) {
        throw new MismatchedMethodException("Can't call method " + s
                + " on type data!");
    }

}
