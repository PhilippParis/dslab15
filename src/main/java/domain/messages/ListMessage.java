package domain.messages;

import domain.IMessage;
import executors.IMessageExecutor;

import java.net.SocketAddress;

/**
 * !list
 * lists all online users in alphabetical order
 */
public class ListMessage extends UDPMessage {

    public ListMessage() {
    }

    @Override
    public void execute(IMessageExecutor executor) {
        executor.executeListMessage(this);
    }

    @Override
    public String toString() {
        return "ListMessage{}";
    }

}
