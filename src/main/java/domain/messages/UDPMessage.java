package domain.messages;

import domain.IMessage;

import java.net.SocketAddress;

/**
 * UDP message
 * all messages send over UDP must extend this class
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
