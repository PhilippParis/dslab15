package client;

import channels.IChannel;
import executors.IMessageExecutor;
import executors.IMessageExecutorFactory;
import service.IClientService;
import service.IConnectionService;

import java.io.PrintStream;

/**
 * Created by phili on 10/20/15.
 */
public class ClientMessageExecutorFactory implements IMessageExecutorFactory {
    private PrintStream userResponseStream;
    private IConnectionService connectionService;
    private IClientService clientService;

    public ClientMessageExecutorFactory(PrintStream userResponseStream, IConnectionService connectionService, IClientService clientService) {
        this.userResponseStream = userResponseStream;
        this.connectionService = connectionService;
        this.clientService = clientService;
    }

    @Override
    public IMessageExecutor create(IChannel channel) {
        return new ClientMessageExecutor(userResponseStream, connectionService, clientService, channel);
    }
}
