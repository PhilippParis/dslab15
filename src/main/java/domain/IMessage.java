package domain;

import executors.IMessageExecutor;

/**
 * Message Interface
 */
public interface IMessage {
    void execute(IMessageExecutor executor);
}
