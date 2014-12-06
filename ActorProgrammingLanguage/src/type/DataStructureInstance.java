/*
 *
 */
package type;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import parser.ExpressionNode;

// TODO: Auto-generated Javadoc
/**
 * The Class DataStructureInstance.
 */
public class DataStructureInstance {

    /**
     * The type of a data structure.
     * <p>
     * Subtypes are represented like this:
     * <p>
     * Parent$Child
     */
    public final String type;

    /** The fields. */
    public final Map<String, ExpressionNode> fields;

    /**
     * Instantiates a new data structure instance.
     *
     * @param type
     *            the type
     * @param fields
     *            the fields
     */
    public DataStructureInstance(final String type,
            final Map<String, ExpressionNode> fields) {
        this.type = type;
        this.fields = new HashMap<>(fields);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (fields.isEmpty()) {
            return type + "{}";
        }
        
        final StringBuilder fieldsAndValues = new StringBuilder();
        for (final Entry<String, ExpressionNode> node : fields.entrySet()) {
            fieldsAndValues.append(node.getKey()).append(" = ")
                    .append(node.getValue()).append(',');
            
        }
        // remove last comma
        return type
                + "{"
                + fieldsAndValues.toString().substring(0,
                        fieldsAndValues.length() - 1) + "}";
    }
}
