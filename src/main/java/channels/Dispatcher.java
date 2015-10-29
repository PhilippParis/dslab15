package channels;

import service.IConnectionService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Created by phili on 10/20/15.
 */
public class Dispatcher implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(Dispatcher.class.getName());
    private ServerSocket serverSocket;
    private IConnectionService connectionService;

    public Dispatcher(IConnectionService connectionService, ServerSocket serverSocket) {
        this.connectionService = connectionService;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run()  {
        LOGGER.info("Dispatcher started");
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                connectionService.addChannel(createTCPChannel(socket));
            }
        } catch (IOException e) { }
        LOGGER.info("Dispatcher stopped");
    }

    public IChannel createTCPChannel(Socket socket) {
        return new TCPChannel(socket, connectionService);
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
