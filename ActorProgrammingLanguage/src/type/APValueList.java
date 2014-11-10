/*
 * @author Kyran Adams
 */
package type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import parser.ExpressionNode;

// TODO: Auto-generated Javadoc
/**
 * The Class APValueList.
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
        setValue(Collections.unmodifiableList(new ArrayList<>(expressionNode)));
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        boolean isString = true;
        for (final Object o : getValue()) {
            if (!(o instanceof Character)) {
                isString = false;
                break;
            }
        }
        if (isString) {
            final List<Character> characters = getValue();
            final StringBuilder builder = new StringBuilder(characters.size());
            for (final Character ch : characters) {
                builder.append(ch);
            }
            return builder.toString();
        }
        return "[" + getValue() + "]";
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
                if (arg.getClass() == APValueList.class) {
                    return new APValueList(append(getValue(),
                            (List<ExpressionNode>) arg.getValue()));
                }
            case MULTIPLY:
                if (arg.getClass() == APValueNum.class) {
                    return new APValueList(multiply(getValue(),
                            (BigDecimal) arg.getValue()));
                }
        }
        
        throw new MismatchedMethodException("Can't call method " + method
                + " on type list!");
    }

    private List<ExpressionNode> multiply(final List<ExpressionNode> value,
            BigDecimal value2) {
        boolean negative = false;
        if (value2.compareTo(BigDecimal.ZERO) < 0) {
            negative = true;
            value2 = value2.negate();
        }

        final List<ExpressionNode> b = new LinkedList<ExpressionNode>();
        
        // if our number is greater than one
        for (; value2.compareTo(BigDecimal.ONE) > 0; value2 = value2
                .subtract(BigDecimal.ONE)) {
            b.addAll(value);
        }
        b.addAll(value.subList(0, (int) (value.size() * value2.doubleValue())));
        if (negative) {
            Collections.reverse(b);
        }
        return b;
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
}
