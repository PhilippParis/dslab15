package executors;

import domain.IChannel;
import domain.IMessage;
import domain.User;
import domain.messages.*;

import java.io.PrintStream;

/**
 * Created by phili on 10/20/15.
 */
public class ClientMessageExecutor implements IMessageExecutor {
    private PrintStream userResponseStream;
    private IChannel channel;

    public ClientMessageExecutor(PrintStream userResponseStream, IChannel channel) {
        this.userResponseStream = userResponseStream;
        this.channel = channel;
    }

    @Override
    public void executeLoginMessage(LoginMessage message) {

    }

    @Override
    public void executeLogoutMessage(LogoutMessage message) {

    }

    @Override
    public void executeSendMessage(SendMessage message) {

    }

    @Override
    public void executeRegisterMessage(RegisterMessage message) {

    }

    @Override
    public void executeLookupMessage(LookupMessage message) {

    }

    @Override
    public void executeListMessage(ListMessage message) {

    }
}
