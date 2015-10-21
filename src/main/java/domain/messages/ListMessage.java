package domain.messages;

import domain.IMessage;
import executors.IMessageExecutor;

import java.net.SocketAddress;

/**
 * Created by phili on 10/20/15.
 */
public class ListMessage implements UDPMessage {
    private SocketAddress address;

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

    @Override
    public void setSocketAddress(SocketAddress address) {
        this.address = address;
    }

    @Override
    public SocketAddress getSocketAddress() {
        return address;
    }
}
