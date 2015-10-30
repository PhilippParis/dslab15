package channels;

import service.IConnectionService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Dispatcher which listenes on a serverSocket and waits for incoming connections
 * creates a TCPChannel for each connection and adds it to the connectionService
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

    /**
     * stops the dispatching of incoming connections and stops the thread
     */
    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
