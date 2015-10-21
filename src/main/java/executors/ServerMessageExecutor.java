package executors;

import domain.IChannel;
import domain.IMessage;
import domain.User;
import domain.messages.*;
import domain.responses.LoginResponse;
import service.ChannelService;
import service.IChannelService;
import service.IUserService;
import service.UserService;

import java.util.logging.Logger;

/**
 * Created by phili on 10/20/15.
 */
public class ServerMessageExecutor extends IMessageExecutor {
    private final static Logger LOGGER = Logger.getLogger(ServerMessageExecutor.class.getName());
    private IChannelService channelService;
    private IUserService userService;
    private IChannel channel;

    public ServerMessageExecutor(IChannelService channelService, IUserService userService, IChannel channel) {
        this.channelService = channelService;
        this.userService = userService;
        this.channel = channel;
    }

    @Override
    public void executeLoginMessage(LoginMessage message) {
        LOGGER.info("message received: " + message.toString());

        // create response
        LoginResponse response = new LoginResponse();

        if (userService.getUser(message.getUsername()) != null) {
            // user already logged in
            response.setSuccessfull(false);
            response.setMessage("user '" + message.getUsername() + "' already logged in");
        } else {
            // create user
            User user = new User(message.getUsername());

            if (userService.authenticate(user, message.getPassword())) {
                // successfully logged in
                userService.addUser(user);
                channel.setUser(user);

                response.setSuccessfull(true);
                response.setMessage("Successfully logged in");
            } else {
                // wrong username / password
                response.setSuccessfull(false);
                response.setMessage("Wrong username or password");
            }
        }

        // send response
        channel.send(response);
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
