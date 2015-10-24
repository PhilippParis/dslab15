package executors;

import domain.IChannel;
import domain.IMessage;
import domain.User;
import domain.messages.*;
import domain.responses.*;

import java.io.PrintStream;
import java.util.logging.Logger;

/**
 * Created by phili on 10/20/15.
 */
public class ClientMessageExecutor extends IMessageExecutor {
    private final static Logger LOGGER = Logger.getLogger(ClientMessageExecutor.class.getName());
    private PrintStream userResponseStream;
    private IChannel channel;

    public ClientMessageExecutor(PrintStream userResponseStream, IChannel channel) {
        this.userResponseStream = userResponseStream;
        this.channel = channel;
    }

    @Override
    public void executeLoginResponse(LoginResponse message) {
        LOGGER.info("message received: " + message.toString());
        userResponseStream.println(message.getMessage());
    }

    @Override
    public void executeLogoutResponse(LogoutResponse message) {
        LOGGER.info("message received: " + message.toString());
        userResponseStream.println(message.getMessage());
    }

    @Override
    public void executeSendResponse(SendResponse message) {
        LOGGER.info("message received: " + message.toString());
        if (!message.isSuccessful()) {
            userResponseStream.println(message.getMessage());
        }
    }

    @Override
    public void executeSendMessage(SendMessage message) {
        LOGGER.info("message received: " + message.toString());
        userResponseStream.println(message.getText());
    }

    @Override
    public void executePrivateMessage(PrivateMessage message) {
        LOGGER.info("message received: " + message.toString());
        userResponseStream.println(message.getText());

        AckResponse response = new AckResponse();
        response.setId(message.getId());
        channel.send(response);

        // TODO stop channel
    }

    public void executeRegisterResponse(RegisterResponse message) {
        LOGGER.info("message received: " + message.toString());
        userResponseStream.println(message.getMessage());
    }

    @Override
    public void executeLookupResponse(LookupResponse message) {
        LOGGER.info("message received: " + message.toString());
    }

    @Override
    public void executeListResponse(ListResponse message) {
        LOGGER.info("message received: " + message.toString());
        userResponseStream.println("Online users:");
        for (String user : message.getOnlineUsers()) {
            userResponseStream.println("* " + user);
        }
    }
}
