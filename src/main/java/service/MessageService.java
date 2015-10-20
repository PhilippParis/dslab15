package service;

import domain.IMessage;
import executors.IMessageExecutorFactory;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
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
    public void execute(final IMessage message) {
        if (message == null) {
            return;
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                message.execute(factory.create());
            }
        });
    }

    @Override
    public byte[] encode(IMessage message) {
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject(message);
            return byteOutputStream.toByteArray();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "error while encoding message");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public IMessage decode(byte[] data) {
        try {
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
            return (IMessage) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "error while decoding message");
        }

        return null;
    }
}
