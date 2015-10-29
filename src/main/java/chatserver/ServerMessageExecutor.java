package chatserver;

import channels.IChannel;
import domain.User;
import domain.messages.*;
import domain.responses.*;
import executors.IMessageExecutor;
import service.IUserService;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by phili on 10/20/15.
 */
public class ServerMessageExecutor extends IMessageExecutor {
    private final static Logger LOGGER = Logger.getLogger(ServerMessageExecutor.class.getName());
    private IUserService userService;
    private IChannel channel;

    public ServerMessageExecutor(IUserService userService, IChannel channel) {
        this.userService = userService;
        this.channel = channel;
    }

    @Override
    public void executeLoginMessage(LoginMessage message) {
        LOGGER.info("message received: " + message.toString());

        // create response
        LoginResponse response = new LoginResponse();
        response.setId(message.getId());

        // get user
        User user = userService.getUser(message.getUsername());

        if (user != null && user.isLoggedIn()) {
            // user already logged in
            response.setSuccessful(false);
            response.setMessage("user '" + message.getUsername() + "' already logged in");
        } else {
            if (user == null) {
                user = new User(message.getUsername(), channel);
            }

            // authenticate user
            if (userService.loginUser(user, message.getPassword())) {
                // successfully logged in
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
        response.setId(message.getId());

        // get user
        User user = userService.getUser(channel);

        if (user == null || !user.isLoggedIn()) {
            // user not logged in
            response.setSuccessful(false);
            response.setMessage("currently not logged in");
        } else {
            user.setLoggedIn(false);

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
        response.setId(message.getId());

        // get user
        User user = userService.getUser(channel);

        // check if user is logged in
        if (user == null || !user.isLoggedIn()) {
            // user not logged in
            response.setMessage("sending message failed: not logged in");
            response.setSuccessful(false);
        } else {
            // user logged in
            response.setMessage("sending message successful");
            response.setSuccessful(true);

            // create new send message with sender name
            SendMessage msg = new SendMessage(user.username() + ": " + message.getText());

            // send msg to all other users
            for (User other : userService.getAllUsers()) {
                if (!other.equals(user)) {
                    other.channel().send(msg);
                }
            }
        }

        // send response to sender
        channel.send(response);
    }

    @Override
    public void executeRegisterMessage(RegisterMessage message) {
        LOGGER.info("message received: " + message.toString());

        // create response
        RegisterResponse response = new RegisterResponse();
        response.setId(message.getId());

        // get user
        User user = userService.getUser(channel);

        if (user == null || !user.isLoggedIn()) {
            // user not logged in
            response.setSuccessful(false);
            response.setMessage("Register failed: not logged in");
        } else {
            response.setSuccessful(true);
            response.setMessage("Successfully registered address for " + user.username());

            // register address
            user.setPrivateAddress(message.getHost(), message.getPort());
        }

        // send response
        channel.send(response);
    }

    @Override
    public void executeLookupMessage(LookupMessage message) {
        LOGGER.info("message received: " + message.toString());

        // create response
        LookupResponse response = new LookupResponse();
        response.setId(message.getId());

        // get user
        User user = userService.getUser(message.getUsername());

        if (user == null || !user.isLoggedIn() ||
                user == null || !user.isLoggedIn() || !user.privateAddressRegistered()) {
            // user not logged in or user not found / address not registered
            response.setSuccessful(false);
        } else {
            response.setSuccessful(true);
            InetSocketAddress address = user.getPrivateAddress();
            response.setHost(address.getHostString());
            response.setPort(address.getPort());
        }

        // send response
        channel.send(response);
    }

    @Override
    public void executeListMessage(ListMessage message) {
        LOGGER.info("message received: " + message.toString());

        // list online users
        ArrayList<String> users = new ArrayList<>();

        for (User user : userService.getAllUsers()) {
            if (user.isLoggedIn()) {
                users.add(user.username());
            }
        }

        // create response
        ListResponse response = new ListResponse();
        response.setId(message.getId());

        response.setSocketAddress(message.getSocketAddress());
        response.setOnlineUsers(users);
        channel.send(response);
    }
}
