package domain.responses;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * Created by phili on 10/21/15.
 */
public class LoginResponse implements IMessage {
    private String message;
    private boolean successfull;

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

    public boolean isSuccessfull() {
        return successfull;
    }

    public void setSuccessfull(boolean successfull) {
        this.successfull = successfull;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "message='" + message + '\'' +
                ", successfull=" + successfull +
                '}';
    }
}
