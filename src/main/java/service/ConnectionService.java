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

    public ConnectionService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void setMessageExecutorFactory(IMessageExecutorFactory factory) {
        this.factory = factory;
    }

    @Override
    public void addChannel(IChannel channel) {
        if (channel == null) {
            return;
        }

        channels.add(channel);
        channel.addOnCloseListener(onCloseListener);

        // start the execution of the channel -> listen for incomming messages
        executorService.execute(channel);
    }

    @Override
    public void closeChannel(IChannel channel) {
        if (channel == null) {
            return;
        }

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
        if (message == null || channel == null) {
            return;
        }
        channel.send(message);
    }

    @Override
    public void forward(IMessage message, IChannel sender) {
        if (sender == null || message == null) {
            return;
        }

        for (IChannel channel : channels) {
            if (!channel.equals(sender)) {
                send(message, channel);
            }
        }
    }

    @Override
    public IMessage sendAndWait(IMessage message, IChannel channel) throws TimeoutException {
        if (message != null && channel != null) {
            return channel.sendAndWait(message);
        }
        return null;
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
