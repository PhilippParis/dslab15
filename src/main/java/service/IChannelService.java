package service;

import channels.IChannel;

import java.util.Collection;


/**
 * Created by phili on 10/20/15.
 */
public interface IChannelService {

    /**
     * adds the channel and starts the execution an a thread
     * @param channel   channel to add
     */
    void addChannel(IChannel channel);

    /**
     * closes the channel
     * @param channel channel to close
     */
    void closeChannel(IChannel channel);

    /**
     * closes all channels
     */
    void closeAll();

    /**
     * @return  returns all channels
     */
    Collection<IChannel> getAllChannels();
}
