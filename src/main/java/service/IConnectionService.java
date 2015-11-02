package service;

import channels.IChannel;
import channels.OnCloseListener;
import domain.IMessage;
import exceptions.InvalidMessageException;
import executors.IMessageExecutorFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeoutException;


/**
 * Connection Service Interface
 */
public interface IConnectionService {

    /**
     * sets the MessageExecutorFactory
     * @param factory factory which creates executors for received messages
     */
    void setMessageExecutorFactory(IMessageExecutorFactory factory);

    /**
     * adds the getChannel and starts the execution in a thread
     * if the onCloseListener was set previously, the listener is added to this getChannel
     * @param channel   getChannel to add
     */
    void addChannel(IChannel channel);

    /**
     * closes the getChannel
     * @param channel getChannel to close
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

    /**
     * specifies the action which will be performed if an getChannel is closed;
     * only for channels which are added after the call of this method
     * @param onCloseListener listener
     */
    void setOnChannelCloseListener(OnCloseListener onCloseListener);

    /**
     * sends the specified message via the specified getChannel
     * returns immediately. the response will be handled by the MessageExecutor
     * @param message message to send
     * @param channel getChannel used for sending
     */
    void send(IMessage message, IChannel channel);

    /**
     * sends the specified message via the specified getChannel and waits
     * for a response; the response will not be executed by the MessageExecutor
     * @param message message to send
     * @param channel getChannel used for sending
     * @throws TimeoutException if no response is received after 1 sec
     */
    IMessage sendAndWait(IMessage message, IChannel channel) throws TimeoutException;

    /**
     * sends the message to all available channels except the sender getChannel
     * @param message message to send
     * @param sender sender which send the message
     */
    void forward(IMessage message, IChannel sender);

    /**
     * executes the message
     * @param message message to execute
     */
    void execute(IMessage message, IChannel channel);

    /**
     * serializes and encodes the message to a byte array for sending
     * @param message message to serialize/encode
     * @return serialized message
     */
    byte[] encode(IMessage message) throws IOException;

    /**
     * decodes the data and returns the message object
     * @param data data sent over the network
     * @return the message object
     */
    IMessage decode(byte[] data) throws InvalidMessageException;
}
