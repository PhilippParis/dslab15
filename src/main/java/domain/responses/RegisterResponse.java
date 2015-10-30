package domain.responses;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * response for the login register
 */
public class RegisterResponse extends IMessage {
    private String message;
    private boolean successful;

    @Override
    public void execute(IMessageExecutor executor) {
        executor.executeRegisterResponse(this);
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
        return "RegisterResponse{" +
                "message='" + message + '\'' +
                ", successful=" + successful +
                '}';
    }
}
