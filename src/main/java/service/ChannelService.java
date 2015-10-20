package service;

import domain.IChannel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Implementation of the channel service interface
 */
public class ChannelService implements IChannelService {
    private ArrayList<IChannel> channels = new ArrayList<>();

    @Override
    public void addChannel(IChannel channel) {
        channels.add(channel);
    }

    @Override
    public Collection<IChannel> getAllChannels() {
        return channels;
    }
}
