/*
 * 
 */
package machine;

import java.util.List;
import java.util.Map;

import parser.ExpressionNode;
import parser.ParserException;
import type.DataStructureInstance;

// TODO: Auto-generated Javadoc
/**
 * The Class DataConstructor.
 */
public class DataConstructor {
    
    /** The name. */
    public final String name;
    
    /** The fields. */
    public final List<String> fields;

    /**
     * Instantiates a new data constructor.
     *
     * @param name the name
     * @param subName the sub name
     * @param fields the fields
     */
    public DataConstructor(final String name, final String subName,
            final List<String> fields) {
        this.name = name + "$" + subName;
        this.fields = fields;
    }
    
    /**
     * Gets the single instance of DataConstructor.
     *
     * @param fields the fields
     * @return single instance of DataConstructor
     */
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
