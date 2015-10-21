package domain;

import service.IMessageService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by phili on 10/20/15.
 */
public class TCPChannel implements IChannel {
    private final static Logger LOGGER = Logger.getLogger(TCPChannel.class.getName());
    private Socket socket;
    private IMessageService messageService;
    private User user = null;

    public TCPChannel(Socket socket, IMessageService messageService) {
        this.socket = socket;
        this.messageService = messageService;

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
    public User user() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        LOGGER.log(Level.INFO, "channel running");
        try {
            while(true) {
                byte[] buffer = new byte[1024];
                socket.getInputStream().read(buffer);
                messageService.execute(messageService.decode(buffer), this);
            }
        } catch (IOException | ClassNotFoundException e) {
        }
        LOGGER.log(Level.INFO, "channel stopped");
    }
}
