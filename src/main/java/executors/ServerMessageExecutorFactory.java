package executors;

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
    public IMessageExecutor create() {
        return new ServerMessageExecutor(channelService, userService);
    }
}
