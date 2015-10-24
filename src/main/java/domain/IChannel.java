package domain;

/**
 * Channel Interface
 */
public interface IChannel extends Runnable {

    /**
     * sends a message via this channel
     * @param message message to send
     */
    void send(IMessage message);

    /**
     * sends the message and waits for a response
     * times out after a specific time
     *
     * response will not be executed by the MessageExecutor
     *
     * @param message message to send
     * @return response (can be null if timeout occurred)
     */
    IMessage sendAndWait(IMessage message);

    /**
     * @return  returns the user associated with this channel.
     *          null if no user logged in via this channel
     */
    User user();

    /**
     * sets the user
     * @param user user
     */
    void setUser(User user);

    /**
     * stops the connection and closes the channel
     */
    void stop();
}
