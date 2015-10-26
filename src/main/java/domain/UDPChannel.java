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
public class UDPChannel extends IChannel {
    private final static Logger LOGGER = Logger.getLogger(UDPChannel.class.getName());
    private DatagramSocket socket;

    public UDPChannel(IMessageService messageService, DatagramSocket socket) {
        super(messageService);
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
    public IMessage read() throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        // set sender address
        UDPMessage message = (UDPMessage) messageService.decode(buffer);
        message.setSocketAddress(packet.getSocketAddress());
        return message;
    }

    @Override
    public void stop() {
        socket.close();
    }
}
