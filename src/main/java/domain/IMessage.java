package domain;

import executors.IMessageExecutor;

import java.io.Serializable;

/**
 * Message Interface
 */
public interface IMessage extends Serializable {
    void execute(IMessageExecutor executor);
}
