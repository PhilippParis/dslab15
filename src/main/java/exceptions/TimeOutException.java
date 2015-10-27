package exceptions;

/**
 * Created by phili on 10/27/15.
 */
public class TimeOutException extends Exception {
    public TimeOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeOutException(String message) {
        super(message);
    }
}
