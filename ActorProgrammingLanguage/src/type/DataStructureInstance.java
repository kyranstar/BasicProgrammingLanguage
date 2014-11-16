package type;

import java.util.HashMap;
import java.util.Map;

import parser.ExpressionNode;

public class DataStructureInstance {
    public final String type;
    public final Map<String, ExpressionNode> fields;
    
    public DataStructureInstance(final String type,
            final Map<String, ExpressionNode> fields) {
        this.type = type;
        this.fields = new HashMap<>(fields);
    }
}
