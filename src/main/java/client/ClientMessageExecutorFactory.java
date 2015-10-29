package client;

import channels.IChannel;
import executors.IMessageExecutor;
import executors.IMessageExecutorFactory;
import service.IConnectionService;

import java.io.PrintStream;

/**
 * Created by phili on 10/20/15.
 */
public class ClientMessageExecutorFactory implements IMessageExecutorFactory {
    private PrintStream userResponseStream;
    private IConnectionService connectionService;

    public ClientMessageExecutorFactory(PrintStream userResponseStream, IConnectionService connectionService) {
        this.userResponseStream = userResponseStream;
        this.connectionService = connectionService;
    }

    @Override
    public IMessageExecutor create(IChannel channel) {
        return new ClientMessageExecutor(userResponseStream, connectionService, channel);
    }
}
