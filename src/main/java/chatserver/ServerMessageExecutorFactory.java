package chatserver;

import channels.IChannel;
import executors.IMessageExecutor;
import executors.IMessageExecutorFactory;
import service.IChannelService;
import service.IUserService;

/**
 * Created by phili on 10/20/15.
 */
public class ServerMessageExecutorFactory implements IMessageExecutorFactory {
    private IUserService userService;

    public ServerMessageExecutorFactory(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public IMessageExecutor create(IChannel channel) {
        return new ServerMessageExecutor(userService, channel);
    }
}
