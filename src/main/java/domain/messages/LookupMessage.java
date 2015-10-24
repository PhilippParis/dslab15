package domain.messages;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * Created by phili on 10/20/15.
 */
public class LookupMessage extends IMessage {
    private String username;

    public LookupMessage(String username) {
        this.username = username;
    }

    @Override
    public void execute(IMessageExecutor executor) {
        executor.executeLookupMessage(this);
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "LookupMessage{" +
                "username='" + username + '\'' +
                '}';
    }
}
