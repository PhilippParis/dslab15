package client;

import channels.IChannel;
import executors.IMessageExecutor;
import executors.IMessageExecutorFactory;
import service.IChannelService;

import java.io.PrintStream;

/**
 * Created by phili on 10/20/15.
 */
public class ClientMessageExecutorFactory implements IMessageExecutorFactory {
    private PrintStream userResponseStream;
    private IChannelService channelService;

    public ClientMessageExecutorFactory(PrintStream userResponseStream, IChannelService channelService) {
        this.userResponseStream = userResponseStream;
        this.channelService = channelService;
    }

    @Override
    public IMessageExecutor create(IChannel channel) {
        return new ClientMessageExecutor(userResponseStream, channelService, channel);
    }
}
