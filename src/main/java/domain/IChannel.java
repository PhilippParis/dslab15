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
     * @return  returns the user associated with this channel.
     *          null if no user logged in via this channel
     */
    User user();

    /**
     * sets the user
     * @param user user
     */
    void setUser(User user);
}
