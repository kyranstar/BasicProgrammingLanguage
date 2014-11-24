package machine;

import java.util.List;
import java.util.Map;

import parser.ExpressionNode;
import parser.ParserException;
import type.DataStructureInstance;

public class DataStructure {
    public final String name;
    public final List<String> fields;
    
    public DataStructure(final String name, final List<String> fields) {
        this.name = name;
        this.fields = fields;
    }

    public DataStructureInstance getInstance(
            final Map<String, ExpressionNode> fields) {
        for (final String s : this.fields) {
            if (!fields.containsKey(s)) {
                throw new ParserException(
                        "Parameters must match data structure constructor");
            }
        }
        for (final String s : fields.keySet()) {
            if (!this.fields.contains(s)) {
                throw new ParserException(
                        "Parameters must match data structure constructor");
            }
        }
        return new DataStructureInstance(name, fields);
    }
}
