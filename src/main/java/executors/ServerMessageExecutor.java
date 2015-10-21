package executors;

import domain.IChannel;
import domain.User;
import domain.messages.*;
import domain.responses.LoginResponse;
import domain.responses.LogoutResponse;
import domain.responses.SendResponse;
import service.IChannelService;
import service.IUserService;

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

        if (channel.user() != null && channel.user().isLoggedIn()) {
            // user already logged in
            response.setSuccessful(false);
            response.setMessage("user '" + message.getUsername() + "' already logged in");
        } else {
            if (channel.user() == null) {
                // create user
                User user = new User(message.getUsername(), channel);
                userService.addUser(user);
                channel.setUser(user);
            }

            // authenticate user
            if (userService.authenticate(channel.user(), message.getPassword())) {
                // successfully logged in
                channel.user().setLoggedIn(true);
                response.setSuccessful(true);
                response.setMessage("Successfully logged in");
            } else {
                // wrong username / password
                response.setSuccessful(false);
                response.setMessage("Wrong username or password");
            }
        }

        // send response
        channel.send(response);
    }

    @Override
    public void executeLogoutMessage(LogoutMessage message) {
        LOGGER.info("message received: " + message.toString());

        // create response
        LogoutResponse response = new LogoutResponse();

        if (channel.user() == null || !channel.user().isLoggedIn()) {
            // user not logged in
            response.setSuccessful(false);
            response.setMessage("currently not logged in");
        } else {
            channel.user().setLoggedIn(false);

            response.setSuccessful(true);
            response.setMessage("Successfully logged out");
        }

        // send response
        channel.send(response);
    }

    @Override
    public void executeSendMessage(SendMessage message) {
        LOGGER.info("message received: " + message.toString());

        // create response for user
        SendResponse response = new SendResponse();

        // check if user is logged in
        if (channel.user() == null || !channel.user().isLoggedIn()) {
            // user not logged in
            response.setMessage("sending message failed: not logged in");
            response.setSuccessful(false);
        } else {
            // user logged in
            response.setMessage("sending message successful");
            response.setSuccessful(true);

            // create new send message with sender name
            SendMessage msg = new SendMessage(channel.user().username() + ": " + message.getText());

            // send msg to all other users
            for (User user : userService.getAllUsers()) {
                if (!user.equals(channel.user())) {
                    user.channel().send(msg);
                }
            }
        }

        // send response to sender
        channel.send(response);
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
