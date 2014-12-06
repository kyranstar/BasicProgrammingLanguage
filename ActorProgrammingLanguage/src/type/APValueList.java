/*
 * @author Kyran Adams
 */
package type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import parser.ExpressionNode;

// TODO: Auto-generated Javadoc
/**
 * The Class APValueList. Represents a list of expressions.
 *
 * @author Kyran Adams
 * @version $Revision: 1.0 $
 */
public class APValueList extends APValue<List> {

    /**
     * Instantiates a new AP value bool.
     *
     * @param expressionNode
     *            the expression node
     */
    public APValueList(final List<ExpressionNode> expressionNode) {
        setValue(new ArrayList<>(expressionNode));
    }
    
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final List<Object> characters = getValue();
        final StringBuilder builder = new StringBuilder(characters.size());
        for (final Object ch : characters) {
            builder.append(ch);
        }
        return builder.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see type.APValue#callMethod(type.APValue.Methods, type.APValue)
     */
    /**
     * Method callMethod.
     *
     * @param method
     *            Operators
     * @param arg
     *            APValue
     * @return APValue
     */
    @Override
    public APValue<?> callMethod(final Operators method, final APValue arg) {
        switch (method) {
            case ADD:
                if (arg instanceof APValueList) {
                    return new APValueList(append(getValue(),
                            (List<ExpressionNode>) arg.getValue()));
                }
                break;
            case MULTIPLY:
                if (arg instanceof APValueNum) {
                    return new APValueList(multiply(getValue(),
                            (APNumber) arg.getValue()));
                }
                break;
            case EQUAL:
                if (arg instanceof APValueList) {
                    return new APValueBool(getValue().equals(arg.getValue()));
                }
                break;
        }

        throw new MismatchedMethodException("Can't call method " + method
                + " on type list with param " + arg);
    }

    /**
     * Multiply list by number.
     *
     * @param value
     *            the value
     * @param value2
     *            the value2
     * @return the list
     */
    private List<ExpressionNode> multiply(final List<ExpressionNode> value,
            APNumber value2) {
        boolean negative = false;
        if (value2.compareTo(APNumber.ZERO) < 0) {
            negative = true;
            value2 = value2.negate();
        }

        final List<ExpressionNode> finalList = new LinkedList<ExpressionNode>();

        // if our number is greater than one
        for (; value2.compareTo(APNumber.ONE) > 0; value2 = value2
                .subtract(APNumber.ONE)) {
            finalList.addAll(value);
        }
        finalList.addAll(value.subList(0, (int) (value.size() * value2.doubleValue())));
        if (negative) {
            Collections.reverse(finalList);
        }
        return finalList;
    }

    /**
     * Append two lists.
     *
     * @param value
     *            the value
     * @param value2
     *            the value2
     *
     * @return the list
     */
    private List<ExpressionNode> append(final List<ExpressionNode> value,
            final List<ExpressionNode> value2) {
        final List<ExpressionNode> newList = new ArrayList<>(value.size()
                + value2.size());
        newList.addAll(value);
        newList.addAll(value2);
        return newList;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see type.APValue#getType()
     */
    @Override
    public String getType() {
        return "List";
    }
}
