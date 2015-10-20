package executors;

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
    public IMessageExecutor create() {
        return new ClientMessageExecutor(userResponseStream);
    }
}
