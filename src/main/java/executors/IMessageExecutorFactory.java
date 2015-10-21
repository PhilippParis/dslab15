package executors;

import domain.IChannel;
import domain.User;

/**
 * Created by phili on 10/20/15.
 */
public interface IMessageExecutorFactory {

    /**
     * @return returns a message executor
     */
    IMessageExecutor create(IChannel channel);
}
