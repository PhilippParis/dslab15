package executors;

import domain.IChannel;
import domain.User;
import service.IChannelService;
import service.IUserService;

/**
 * Created by phili on 10/20/15.
 */
public class ServerMessageExecutorFactory implements IMessageExecutorFactory {
    private IChannelService channelService;
    private IUserService userService;

    public ServerMessageExecutorFactory(IChannelService channelService, IUserService userService) {
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public IMessageExecutor create(IChannel channel) {
        return new ServerMessageExecutor(channelService, userService, channel);
    }
}
