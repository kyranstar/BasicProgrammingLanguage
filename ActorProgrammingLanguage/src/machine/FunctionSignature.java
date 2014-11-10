package machine;

/**
 */
public class FunctionSignature {
    public final String functionName;
    
    /**
     * Constructor for FunctionSignature.
     *
     * @param name
     *            String
     * @param parameters
     *            int
     */
    public FunctionSignature(final String name) {
        functionName = name;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + (functionName == null ? 0 : functionName.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FunctionSignature other = (FunctionSignature) obj;
        if (functionName == null) {
            if (other.functionName != null) {
                return false;
            }
        } else if (!functionName.equals(other.functionName)) {
            return false;
        }
        return true;
    }
    
    /**
     * Method toString.
     *
     * @return String
     */
    @Override
    public String toString() {
        if (functionName != null) {
            return functionName;
        }
        return "lambda";
    }
    
}
