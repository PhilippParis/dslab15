package domain.messages;

import domain.IMessage;
import executors.IMessageExecutor;

import java.net.SocketAddress;

/**
 * Created by phili on 10/20/15.
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
