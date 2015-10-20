package service;

import domain.IChannel;

import java.util.Collection;


/**
 * Created by phili on 10/20/15.
 */
public interface IChannelService {

    /**
     * adds a channel
     * @param channel   channel to add
     */
    void addChannel(IChannel channel);

    /**
     * @return  returns all channels
     */
    Collection<IChannel> getAllChannels();
}
