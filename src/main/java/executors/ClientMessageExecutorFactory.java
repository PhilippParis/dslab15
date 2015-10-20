package executors;

import domain.IChannel;
import domain.User;

import java.io.PrintStream;

/**
 * Created by phili on 10/20/15.
 */
public class ClientMessageExecutorFactory implements IMessageExecutorFactory {
    private PrintStream userResponseStream;

    public ClientMessageExecutorFactory(PrintStream userResponseStream) {
        this.userResponseStream = userResponseStream;
    }

    @Override
    public IMessageExecutor create(IChannel channel, User user) {
        ClientMessageExecutor executor = new ClientMessageExecutor(userResponseStream);
        executor.setSenderInfo(channel, user);
        return executor;
    }
}
