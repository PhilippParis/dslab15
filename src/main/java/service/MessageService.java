package service;

import domain.IMessage;
import executors.IMessageExecutorFactory;

import java.util.concurrent.ExecutorService;

/**
 * Created by phili on 10/20/15.
 */
public class MessageService implements IMessageService {
    private IMessageExecutorFactory factory;
    private ExecutorService executorService;

    public MessageService(IMessageExecutorFactory factory, ExecutorService executorService) {
        this.factory = factory;
        this.executorService = executorService;
    }

    @Override
    public void execute(final IMessage message) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                message.execute(factory.create());
            }
        });
    }
}
