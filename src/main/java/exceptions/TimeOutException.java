package exceptions;

/**
 * Exception which indicates the occurrence of a timeout
 */
public class TimeOutException extends Exception {
    public TimeOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeOutException(String message) {
        super(message);
    }
}
