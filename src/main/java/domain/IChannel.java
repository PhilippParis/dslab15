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
}
