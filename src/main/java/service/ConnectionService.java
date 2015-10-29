package service;

import channels.IChannel;
import channels.OnCloseListener;
import domain.IMessage;
import executors.IMessageExecutorFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

/**
 * Implementation of the channel service interface
 */
public class ConnectionService implements IConnectionService {
    private ArrayList<IChannel> channels = new ArrayList<>();
    private ExecutorService executorService;
    private OnCloseListener onCloseListener;
    private IMessageExecutorFactory factory;

    public ConnectionService(ExecutorService executorService, IMessageExecutorFactory factory) {
        this.executorService = executorService;
        this.factory = factory;
    }

    @Override
    public void addChannel(IChannel channel) {
        channels.add(channel);
        channel.addOnCloseListener(onCloseListener);

        // start the execution of the channel -> listen for incomming messages
        executorService.execute(channel);
    }

    @Override
    public void closeChannel(IChannel channel) {
        channels.remove(channel);
        channel.stop();
    }

    public void setOnChannelCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    @Override
    public void closeAll() {
        for (IChannel channel : channels) {
            channel.stop();
        }
    }

    @Override
    public Collection<IChannel> getAllChannels() {
        return channels;
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
    public void send(IMessage message, IChannel channel) {
        channel.send(message);
    }

    @Override
    public void sendAndWait(IMessage message, IChannel channel) throws TimeoutException {
        channel.sendAndWait(message);
    }

    public byte[] encode(IMessage message) throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
        objectOutputStream.writeObject(message);
        return byteOutputStream.toByteArray();
    }

    public IMessage decode(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
        return (IMessage) objectInputStream.readObject();
    }
}
