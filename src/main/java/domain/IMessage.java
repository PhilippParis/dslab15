package domain;

import executors.IMessageExecutor;

import java.io.Serializable;

/**
 * Message Interface
 * contains a ID which is unique for each message.
 * the direct response-message has the same ID
 */
public abstract class IMessage implements Serializable {
    private Long id = -1L;

    /**
     * executes the message
     * @param executor executor
     */
    public abstract void execute(IMessageExecutor executor);

    /**
     * @return returns the ID of the message
     */
    public Long getId() {
        return id;
    }

    /**
     * sets the ID
     * each message must have a unique ID (per Channel)
     * responses must have the same ID as the request
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }
}
