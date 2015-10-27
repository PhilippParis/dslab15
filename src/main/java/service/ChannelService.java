package service;

import channels.IChannel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

/**
 * Implementation of the channel service interface
 */
public class ChannelService implements IChannelService {
    private ArrayList<IChannel> channels = new ArrayList<>();
    private ExecutorService executorService;

    public ChannelService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void addChannel(IChannel channel) {
        channels.add(channel);
        // start the execution of the channel -> listen for incomming messages
        executorService.execute(channel);
    }

    @Override
    public void closeChannel(IChannel channel) {
        channels.remove(channel);
        channel.stop();
    }

    @Override
    public void closeAll() {
        for (IChannel channel : channels) {
            channel.stop();
        }
    }

    @Override
    public Collection<IChannel> getAllChannels() {
        return channels;
    }
}
