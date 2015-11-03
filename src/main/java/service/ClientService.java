package service;

import channels.Dispatcher;
import domain.IMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ClientService
 */
public class ClientService implements IClientService {
    private final static Logger LOGGER = Logger.getLogger(ClientService.class.getName());
    private boolean loggedIn = false;
    private IMessage lastMessage;
    private String username;
    private IConnectionService connectionService;
    private ExecutorService executorService;
    private List<Dispatcher> dispatchers = new ArrayList<>();

    public ClientService(ExecutorService executorService, IConnectionService connectionService) {
        this.executorService = executorService;
        this.connectionService = connectionService;
    }

    @Override
    public void logout() {
        this.loggedIn = false;

        // stop all dispatchers
        for (Dispatcher dispatcher : dispatchers) {
            dispatcher.stop();
        }
    }

    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }

    @Override
    public void login(String username) {
        this.loggedIn = true;
        this.username = username;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public IMessage lastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(IMessage message) {
        this.lastMessage = message;
    }

    @Override
    public Dispatcher createPrivateConnectionDispatcher(int port) {
        // try to start tcp server
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Dispatcher dispatcher = new Dispatcher(connectionService, serverSocket);
            dispatchers.add(dispatcher);
            executorService.execute(dispatcher);
            return dispatcher;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "error creating server socket", e);
            return null;
        }
    }
}
