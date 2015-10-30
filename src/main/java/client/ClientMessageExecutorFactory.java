package client;

import channels.IChannel;
import cli.Shell;
import executors.IMessageExecutor;
import executors.IMessageExecutorFactory;
import service.IClientService;
import service.IConnectionService;

import java.io.PrintStream;

/**
 * Factory which creates ServerMessageExecutors
 */
public class ClientMessageExecutorFactory implements IMessageExecutorFactory {
    private Shell shell;
    private IConnectionService connectionService;
    private IClientService clientService;

    public ClientMessageExecutorFactory(Shell shell, IConnectionService connectionService, IClientService clientService) {
        this.shell = shell;
        this.connectionService = connectionService;
        this.clientService = clientService;
    }

    @Override
    public IMessageExecutor create(IChannel channel) {
        return new ClientMessageExecutor(shell, connectionService, clientService, channel);
    }
}
