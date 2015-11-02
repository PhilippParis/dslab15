package domain.responses;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * Created by phili on 11/2/15.
 */
public class ErrorResponse extends IMessage {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    @Override
    public void execute(IMessageExecutor executor) {
        executor.executeErrorResponse(this);
    }

    public String getMessage() {
        return message;
    }
}
