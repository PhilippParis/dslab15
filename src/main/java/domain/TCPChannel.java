package domain;

import service.IMessageService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by phili on 10/20/15.
 */
public class TCPChannel extends IChannel {
    private final static Logger LOGGER = Logger.getLogger(TCPChannel.class.getName());
    private Socket socket;

    public TCPChannel(Socket socket, IMessageService messageService) {
        super(messageService);
        this.socket = socket;
    }

    @Override
    public void send(IMessage message) {
        try {
            socket.getOutputStream().write(messageService.encode(message));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "failed to send message: " + e.toString());
        }
    }

    @Override
    public IMessage read() throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[1024];
        socket.getInputStream().read(buffer);
        return  messageService.decode(buffer);
    }

    @Override
    public void stop() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
