/*
 *
 */
package parser;

/**
 * The Class Debug which holds many debugging methods.
 */
public final class Debug {

    /**
     * Finds out the index of "this code" in the returned stack trace
     */
    private static final int CLIENT_CODE_STACK_INDEX;

    private Debug() {

    }

    static {
        // Finds out the index of "this code" in the returned stack trace -
        // funny but it differs in JDK 1.5 and 1.6
        int i = 0;
        for (final StackTraceElement ste : Thread.currentThread()
                .getStackTrace()) {
            i++;
            if (ste.getClassName().equals(Debug.class.getName())) {
                break;
            }
        }
        CLIENT_CODE_STACK_INDEX = i;
    }

    /**
     * Returns the name of the current method calling this method.
     *
     * @return the string
     */
    public static String currentMethodName() {
        return Thread.currentThread().getStackTrace()[CLIENT_CODE_STACK_INDEX]
                .getMethodName();
    }
    
    /**
     * Returns the name of the method callsUp calls above our call.
     * <p>
     * For example:
     * <p>
     * {@code a -> b -> c -> callerMethod(0) returns c}
     * <p>
     * {@code a -> b -> c -> callerMethod(1) returns b}
     * <p>
     * {@code a -> b -> c -> callerMethod(2) returns a}
     *
     * @param callsUp
     *            the amount of calls up
     * @return the string
     */
    public static String callerMethod(final int callsUp) {
        return Thread.currentThread().getStackTrace()[CLIENT_CODE_STACK_INDEX
                + callsUp].getMethodName();
    }
}
