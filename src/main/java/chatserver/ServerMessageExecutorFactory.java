package chatserver;

import channels.IChannel;
import executors.IMessageExecutor;
import executors.IMessageExecutorFactory;
import service.IConnectionService;
import service.IUserService;

/**
 * Factory which creates ServerMessageExecutors
 */
public class ServerMessageExecutorFactory implements IMessageExecutorFactory {
    private IUserService userService;
    private IConnectionService connectionService;

    public ServerMessageExecutorFactory(IUserService userService, IConnectionService connectionService) {
        this.userService = userService;
        this.connectionService = connectionService;
    }

    @Override
    public IMessageExecutor create(IChannel channel) {
        return new ServerMessageExecutor(userService, connectionService, channel);
    }
}
