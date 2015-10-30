package domain.responses;

import domain.messages.UDPMessage;
import executors.IMessageExecutor;

import java.net.SocketAddress;
import java.util.ArrayList;

/**
 * Response for the list message
 * contains a list of online users
 */
public class ListResponse extends UDPMessage {
    private SocketAddress address;
    private ArrayList<String> onlineUsers;

    @Override
    public void setSocketAddress(SocketAddress address) {
        this.address = address;
    }

    @Override
    public SocketAddress getSocketAddress() {
        return address;
    }

    @Override
    public void execute(IMessageExecutor executor) {
        executor.executeListResponse(this);
    }

    public ArrayList<String> getOnlineUsers() {
        return onlineUsers;
    }

    public void setOnlineUsers(ArrayList<String> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    @Override
    public String toString() {
        return "ListResponse{" +
                "address=" + address +
                ", onlineUsers=" + onlineUsers +
                '}';
    }
}
