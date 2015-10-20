package executors;

import domain.IMessage;

import java.io.PrintStream;

/**
 * Created by phili on 10/20/15.
 */
public class ClientMessageExecutor implements IMessageExecutor {
    private PrintStream userResponseStream;

    public ClientMessageExecutor(PrintStream userResponseStream) {
        this.userResponseStream = userResponseStream;
    }

    @Override
    public void executeLoginMessage(IMessage message) {

    }
}
