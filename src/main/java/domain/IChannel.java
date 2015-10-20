package domain;

/**
 * Channel Interface
 */
public interface IChannel {

    /**
     * sends a message via this channel
     * @param message message to send
     */
    void send(IMessage message);
}
