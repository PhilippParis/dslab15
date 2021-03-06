package domain.responses;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * response for the login message
 */
public class LoginResponse extends IMessage {
    private String message;
    private boolean successful;

    @Override
    public void execute(IMessageExecutor executor) {
        executor.executeLoginResponse(this);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "message='" + message + '\'' +
                ", successful=" + successful +
                '}';
    }
}
