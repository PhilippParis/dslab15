package channels;

import domain.IMessage;
import exceptions.InvalidMessageException;
import service.IConnectionService;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Connection Channel which uses TCP
 */
public class TCPChannel extends IChannel {
    private final static Logger LOGGER = Logger.getLogger(TCPChannel.class.getName());
    private Socket socket;
    private IConnectionService connectionService;

    public TCPChannel(Socket socket, IConnectionService connectionService) {
        super(connectionService);
        this.socket = socket;
        this.connectionService = connectionService;
    }

    @Override
    public void send(IMessage message) {
        try {
            socket.getOutputStream().write(connectionService.encode(message));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "failed to send message: ", e);
        }
    }

    @Override
    protected IMessage read() throws IOException, InvalidMessageException {
        byte[] buffer = new byte[1024];
        socket.getInputStream().read(buffer);
        return connectionService.decode(buffer);
    }

    @Override
    public void stop() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "failed to close socket", e);
        }
    }
}
