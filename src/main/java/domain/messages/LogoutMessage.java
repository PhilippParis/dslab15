package domain.messages;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * Created by phili on 10/20/15.
 */
public class LogoutMessage implements IMessage {

    public LogoutMessage() {

    }

    @Override
    public void execute(IMessageExecutor executor) {
        executor.executeLogoutMessage(this);
    }

    @Override
    public String toString() {
        return "LogoutMessage{}";
    }
}
