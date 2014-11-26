package type;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import parser.ExpressionNode;

public class DataStructureInstance {
    public final String type;
    public final Map<String, ExpressionNode> fields;
    
    public DataStructureInstance(final String type,
            final Map<String, ExpressionNode> fields) {
        this.type = type;
        this.fields = new HashMap<>(fields);
    }

    @Override
    public String toString() {
        if (fields.isEmpty()) {
            return type + "{}";
        }

        final StringBuilder fieldsAndValues = new StringBuilder();
        for (final Entry<String, ExpressionNode> node : fields.entrySet()) {
            fieldsAndValues.append(node.getKey()).append(" = ")
            .append(node.getValue()).append(",");

        }
        // remove last comma
        return type
                + "{"
                + fieldsAndValues.toString().substring(0,
                        fieldsAndValues.length() - 1) + "}";
    }
}
