package domain.messages;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * !lookup <username>
 * performs a lookup of the given username and returns the address (IP:Port) of the
 * the user which has registered the address previously
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
