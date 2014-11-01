/*
 * @author Kyran Adams
 */
package type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parser.ExpressionNode;

// TODO: Auto-generated Javadoc
/**
 * The Class APValueList.
 */
public class APValueList extends APValue<List> {

    /** The Constant TYPE. */
    private static final Class<List> TYPE = List.class;

    /**
     * Instantiates a new AP value bool.
     *
     * @param expressionNode
     *            the expression node
     */
    public APValueList(final List<ExpressionNode> expressionNode) {
        setValue(Collections.unmodifiableList(new ArrayList<>(expressionNode)));
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
                    + " must take two list types. Was " + TYPE + " and "
                    + arg.getType());
        }

        switch (method) {
            case ADD:
                return new APValueList(
                        append(getValue(), (List) arg.getValue()));
        }
        throw new MismatchedMethodException("Can't call method " + method
                + " on type list!");
    }
    
    /**
     * Append.
     *
     * @param value
     *            the value
     * @param value2
     *            the value2
     * @return the list
     */
    private List<ExpressionNode> append(final List value, final List value2) {
        final List<ExpressionNode> newList = new ArrayList<>(value.size()
                + value2.size());
        newList.addAll(value);
        newList.addAll(value2);
        return newList;
    }
}
