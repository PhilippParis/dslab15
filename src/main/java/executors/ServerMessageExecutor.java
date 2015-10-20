package executors;

import domain.IChannel;
import domain.IMessage;
import domain.User;
import service.ChannelService;
import service.IChannelService;
import service.IUserService;
import service.UserService;

import java.util.logging.Logger;

/**
 * Created by phili on 10/20/15.
 */
public class ServerMessageExecutor implements IMessageExecutor {
    private final static Logger LOGGER = Logger.getLogger(ServerMessageExecutor.class.getName());
    private IChannelService channelService;
    private IUserService userService;
    private IChannel channel;
    private User user;

    public ServerMessageExecutor(IChannelService channelService, IUserService userService) {
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public void setSenderInfo(IChannel channel, User user) {
        this.channel = channel;
        this.user = user;
    }

    @Override
    public void executeLoginMessage(IMessage message) {
        LOGGER.info("message received: " + message.toString());
    }

    @Override
    public void executeLogoutMessage(IMessage message) {
        LOGGER.info("message received: " + message.toString());

    }

    @Override
    public void executeSendMessage(IMessage message) {
        LOGGER.info("message received: " + message.toString());

    }

    @Override
    public void executeRegisterMessage(IMessage message) {
        LOGGER.info("message received: " + message.toString());

    }

    @Override
    public void executeLookupMessage(IMessage message) {
        LOGGER.info("message received: " + message.toString());

    }

    @Override
    public void executeListMessage(IMessage message) {
        LOGGER.info("message received: " + message.toString());

    }
}
