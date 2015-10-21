package executors;

import domain.IChannel;
import domain.IMessage;
import domain.User;
import domain.messages.*;
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

    public ServerMessageExecutor(IChannelService channelService, IUserService userService, IChannel channel) {
        this.channelService = channelService;
        this.userService = userService;
        this.channel = channel;
    }

    @Override
    public void executeLoginMessage(LoginMessage message) {
        LOGGER.info("message received: " + message.toString());

    }

    @Override
    public void executeLogoutMessage(LogoutMessage message) {
        LOGGER.info("message received: " + message.toString());

    }

    @Override
    public void executeSendMessage(SendMessage message) {
        LOGGER.info("message received: " + message.toString());

    }

    @Override
    public void executeRegisterMessage(RegisterMessage message) {
        LOGGER.info("message received: " + message.toString());

    }

    @Override
    public void executeLookupMessage(LookupMessage message) {
        LOGGER.info("message received: " + message.toString());

    }

    @Override
    public void executeListMessage(ListMessage message) {
        LOGGER.info("message received: " + message.toString());

    }
}
