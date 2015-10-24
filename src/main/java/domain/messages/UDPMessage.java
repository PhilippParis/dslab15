package domain.messages;

import domain.IMessage;

import java.net.SocketAddress;

/**
 * Created by phili on 10/21/15.
 */
public abstract class UDPMessage extends IMessage {
    private SocketAddress address;

    public void setSocketAddress(SocketAddress address) {
        this.address = address;
    }

    public SocketAddress getSocketAddress() {
        return address;
    }
}
