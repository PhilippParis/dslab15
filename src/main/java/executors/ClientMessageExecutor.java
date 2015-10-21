package executors;

import domain.IChannel;
import domain.IMessage;
import domain.User;
import domain.messages.*;
import domain.responses.LoginResponse;
import domain.responses.LogoutResponse;

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
    public void executeSendMessage(SendMessage message) {

    }
}
