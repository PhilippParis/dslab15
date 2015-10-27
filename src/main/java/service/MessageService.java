package service;

import channels.IChannel;
import domain.IMessage;
import executors.IMessageExecutorFactory;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

/**
 * Created by phili on 10/20/15.
 */
public class MessageService implements IMessageService {
    private final static Logger LOGGER = Logger.getLogger(MessageService.class.getName());
    private IMessageExecutorFactory factory;
    private ExecutorService executorService;

    public MessageService(IMessageExecutorFactory factory, ExecutorService executorService) {
        this.factory = factory;
        this.executorService = executorService;
    }

    @Override
    public void execute(final IMessage message, final IChannel channel) {
        if (message == null || channel == null) {
            return;
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                message.execute(factory.create(channel));
            }
        });
    }

    @Override
    public byte[] encode(IMessage message) throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
        objectOutputStream.writeObject(message);
        return byteOutputStream.toByteArray();
    }

    @Override
    public IMessage decode(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
        return (IMessage) objectInputStream.readObject();
    }
}
