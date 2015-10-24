package domain.responses;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * Created by phili on 10/21/15.
 */
public class LogoutResponse extends IMessage {
    private String message;
    private boolean successful;

    @Override
    public void execute(IMessageExecutor executor) {
        executor.executeLogoutResponse(this);
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
        return "LogoutResponse{" +
                "message='" + message + '\'' +
                ", successful=" + successful +
                '}';
    }
}
