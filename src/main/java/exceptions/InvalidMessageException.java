package exceptions;

/**
 * Exception indicating a invalid or unexpected message
 */
public class InvalidMessageException extends Exception {
    public InvalidMessageException(Throwable cause) {
        super(cause);
    }
}
