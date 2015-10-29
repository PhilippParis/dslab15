package channels;

import domain.IMessage;
import domain.messages.UDPMessage;
import service.IConnectionService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by phili on 10/21/15.
 */
public class UDPChannel extends IChannel {
    private final static Logger LOGGER = Logger.getLogger(UDPChannel.class.getName());
    private DatagramSocket socket;
    private IConnectionService connectionService;

    public UDPChannel(IConnectionService connectionService, DatagramSocket socket) {
        super(connectionService);
        this.socket = socket;
        this.connectionService = connectionService;
    }

    @Override
    public void send(IMessage message) {
        // create and send datagram packet
        try {
            byte[] data = connectionService.encode(message);
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
        UDPMessage message = (UDPMessage) connectionService.decode(buffer);
        message.setSocketAddress(packet.getSocketAddress());
        return message;
    }

    @Override
    public void stop() {
        socket.close();
    }
}
