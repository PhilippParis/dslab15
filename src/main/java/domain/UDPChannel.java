package domain;

import domain.messages.UDPMessage;
import service.IMessageService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by phili on 10/21/15.
 */
public class UDPChannel implements IChannel {
    private final static Logger LOGGER = Logger.getLogger(UDPChannel.class.getName());
    private IMessageService messageService;
    private DatagramSocket socket;

    public UDPChannel(IMessageService messageService, DatagramSocket socket) {
        this.messageService = messageService;
        this.socket = socket;
    }

    @Override
    public void send(IMessage message) {
        // create and send datagram packet
        try {
            byte[] data = messageService.encode(message);
            DatagramPacket packet = new DatagramPacket(data, data.length,((UDPMessage)message).getSocketAddress());
            socket.send(packet);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "failed to send message: " + e.toString());
        }
    }

    @Override
    public User user() {
        return null;
    }

    @Override
    public void setUser(User user) {
        // not needed
    }

    @Override
    public void run() {
        LOGGER.log(Level.INFO, "UDP channel started");
        try {
            while(true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                // set sender address
                UDPMessage message = (UDPMessage) messageService.decode(buffer);
                message.setSocketAddress(packet.getSocketAddress());

                // execute
                messageService.execute(message, this);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        LOGGER.log(Level.INFO, " UDP channel stopped");
    }
}
