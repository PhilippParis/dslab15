package domain;

import service.IMessageService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by phili on 10/20/15.
 */
public class TCPChannel implements IChannel {
    private final static Logger LOGGER = Logger.getLogger(TCPChannel.class.getName());
    private Socket socket;
    private IMessageService messageService;
    private User user = null;
    private AtomicLong messageID = new AtomicLong();

    private ConcurrentHashMap<Long, Task> tasks = new ConcurrentHashMap<Long, Task>();

    public TCPChannel(Socket socket, IMessageService messageService) {
        this.socket = socket;
        this.messageService = messageService;
    }

    @Override
    public void send(IMessage message) {
        try {
            socket.getOutputStream().write(messageService.encode(message));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "failed to send message: " + e.toString());
        }
    }

    @Override
    public IMessage sendAndWait(IMessage message) {
        // create unique message id
        long id = messageID.incrementAndGet();

        // create task
        message.setId(id);
        Task task = new Task(message);
        tasks.put(id, task);

        // send message
        send(message);

        // wait for response
        task.await(1000);

        // remove task and return response
        tasks.remove(id);
        return task.getResponse();
    }

    @Override
    public User user() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void stop() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        LOGGER.log(Level.INFO, "TCP channel started");
        try {
            while(true) {
                byte[] buffer = new byte[1024];
                socket.getInputStream().read(buffer);

                IMessage response = messageService.decode(buffer);

                if (tasks.containsKey(response.getId())) {
                    // if send and wait is active -> notify waiting thread
                    Task task = tasks.get(response.getId());
                    task.setResponse(response);
                    task.signal();
                } else {
                    // send and wait not active -> use executor
                    messageService.execute(response, this);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
        }
        LOGGER.log(Level.INFO, "TCP channel stopped");
    }
}
