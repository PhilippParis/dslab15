package executors;

/**
 * Created by phili on 10/20/15.
 */
public interface IMessageExecutorFactory {

    /**
     * @return returns a message executor
     */
    IMessageExecutor create();
}
