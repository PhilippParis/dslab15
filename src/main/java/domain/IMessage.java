package domain;

import executors.IMessageExecutor;

import java.io.Serializable;

/**
 * Message Interface
 */
public abstract class IMessage implements Serializable {
    private Long id = -1L;

    public abstract void execute(IMessageExecutor executor);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
