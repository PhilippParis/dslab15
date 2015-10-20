package executors;

import domain.IChannel;
import domain.IMessage;
import domain.User;

import java.io.PrintStream;

/**
 * Created by phili on 10/20/15.
 */
public class ClientMessageExecutor implements IMessageExecutor {
    private PrintStream userResponseStream;
    private IChannel channel;
    private User user;

    public ClientMessageExecutor(PrintStream userResponseStream) {
        this.userResponseStream = userResponseStream;
    }

    @Override
    public void setSenderInfo(IChannel channel, User user) {
        this.channel = channel;
        this.user = user;
    }

    @Override
    public void executeLoginMessage(IMessage message) {

    }

    @Override
    public void executeLogoutMessage(IMessage message) {

    }

    @Override
    public void executeSendMessage(IMessage message) {

    }

    @Override
    public void executeRegisterMessage(IMessage message) {

    }

    @Override
    public void executeLookupMessage(IMessage message) {

    }

    @Override
    public void executeListMessage(IMessage message) {

    }
}
