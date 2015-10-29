package client;

import channels.IChannel;
import executors.IMessageExecutor;
import executors.IMessageExecutorFactory;
import service.IConnectionService;

import java.io.PrintStream;

/**
 * Created by phili on 10/20/15.
 */
public class ClientMessageExecutorFactory implements IMessageExecutorFactory {
    private PrintStream userResponseStream;
    private IConnectionService channelService;

    public ClientMessageExecutorFactory(PrintStream userResponseStream, IConnectionService channelService) {
        this.userResponseStream = userResponseStream;
        this.channelService = channelService;
    }

    @Override
    public IMessageExecutor create(IChannel channel) {
        return new ClientMessageExecutor(userResponseStream, channelService, channel);
    }
}
