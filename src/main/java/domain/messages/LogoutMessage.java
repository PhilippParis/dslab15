package domain.messages;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * !logout
 * log out currently logged in user
 */
public class LogoutMessage extends IMessage {

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
