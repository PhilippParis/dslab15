package domain.messages;

import domain.IMessage;

import java.net.SocketAddress;

/**
 * Created by phili on 10/21/15.
 */
public interface UDPMessage extends IMessage {

    void setSocketAddress(SocketAddress address);

    SocketAddress getSocketAddress();
}
