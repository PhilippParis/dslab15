package executors;

import domain.IMessage;
import service.ChannelService;
import service.IChannelService;
import service.IUserService;

/**
 * Created by phili on 10/20/15.
 */
public class ServerMessageExecutor implements IMessageExecutor {
    private IChannelService channelService;
    private IUserService userService;

    public ServerMessageExecutor(IChannelService channelService, IUserService userService) {
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public void executeLoginMessage(IMessage message) {

    }
}
