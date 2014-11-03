package machine;

public class FunctionSignature {
    public final String functionName;
    public final int parameters;

    public FunctionSignature(final String name, final int parameters) {
        functionName = name;
        this.parameters = parameters;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + (functionName == null ? 0 : functionName.hashCode());
        result = prime * result + parameters;
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
        if (parameters != other.parameters) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "FunctionSignature [functionName=" + functionName
                + ", parameters=" + parameters + "]";
    }

}
