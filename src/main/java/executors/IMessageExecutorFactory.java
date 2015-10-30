package executors;

import channels.IChannel;

/**
 * MessageExecutor factory
 */
public interface IMessageExecutorFactory {

    /**
     * @return returns a message executor
     */
    IMessageExecutor create(IChannel channel);
}
