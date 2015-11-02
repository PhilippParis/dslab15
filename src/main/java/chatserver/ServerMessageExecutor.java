package chatserver;

import channels.IChannel;
import domain.User;
import domain.messages.*;
import domain.responses.*;
import executors.IMessageExecutor;
import service.IConnectionService;
import service.IUserService;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Message Executor for the Server
 * handles all incoming messages
 */
public class ServerMessageExecutor extends IMessageExecutor {
    private final static Logger LOGGER = Logger.getLogger(ServerMessageExecutor.class.getName());
    private IUserService userService;
    private IConnectionService connectionService;
    private IChannel channel;

    public ServerMessageExecutor(IUserService userService, IConnectionService connectionService, IChannel channel) {
        super(channel, connectionService);
        this.userService = userService;
        this.connectionService = connectionService;
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
            // authenticate user
            if (user != null && userService.loginUser(user, message.getPassword())) {
                // successfully logged in
                user.setChannel(channel);
                response.setSuccessful(true);
                response.setMessage("Successfully logged in");
            } else {
                // wrong username / password
                response.setSuccessful(false);
                response.setMessage("Wrong username or password");
            }
        }

        // send response
        connectionService.send(response, channel);
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
            response.setMessage("currently not logged in.");
        } else {
            user.setLoggedIn(false);

            response.setSuccessful(true);
            response.setMessage("Successfully logged out.");
        }

        // send response
        connectionService.send(response, channel);
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

            // forward message to other clients
            for (User otherUser : userService.getAllUsers()) {
                if (!otherUser.equals(user)) {
                    connectionService.send(msg, otherUser.getChannel());
                }
            }
        }

        // send response to sender
        connectionService.send(response, channel);
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
            response.setMessage("Successfully registered address for " + user.username() + ".");

            // register address
            user.setPrivateAddress(message.getHost(), message.getPort());
        }

        // send response
        connectionService.send(response, channel);
    }

    @Override
    public void executeLookupMessage(LookupMessage message) {
        LOGGER.info("message received: " + message.toString());

        // create response
        LookupResponse response = new LookupResponse();
        response.setId(message.getId());

        // get users
        User user = userService.getUser(channel);
        User otherUser = userService.getUser(message.getUsername());

        if (user == null || !user.isLoggedIn()) {
            // user not logged in
            response.setSuccessful(false);
            response.setMessage("lookup failed: not logged in");
        } else if (otherUser == null || !otherUser.isLoggedIn() || !otherUser.privateAddressRegistered()) {
            // other user not logged in or no address registered
            response.setSuccessful(false);
            response.setMessage("lookup failed: Wrong username or user not reachable");
        } else {
            response.setSuccessful(true);
            InetSocketAddress address = otherUser.getPrivateAddress();
            response.setHost(address.getHostString());
            response.setPort(address.getPort());
            response.setMessage(address.getHostString() + ":" + address.getPort());
        }

        // send response
        connectionService.send(response, channel);
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

        // sort list alphabetically
        Collections.sort(users);

        // create response
        ListResponse response = new ListResponse();
        response.setId(message.getId());

        response.setSocketAddress(message.getSocketAddress());
        response.setOnlineUsers(users);

        // send response
        connectionService.send(response, channel);
    }
}
