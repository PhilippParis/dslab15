package domain;

import service.IMessageService;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Channel Interface
 */
public abstract class IChannel implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(IChannel.class.getName());
    private User user;
    private AtomicLong messageID = new AtomicLong();
    private ConcurrentHashMap<Long, Task> tasks = new ConcurrentHashMap<Long, Task>();
    protected IMessageService messageService;

    public IChannel(IMessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * sends a message via this channel
     * @param message message to send
     */
    public abstract void send(IMessage message);

    public abstract IMessage read() throws IOException, ClassNotFoundException;

    /**
     * sends the message and waits for a response
     * times out after a specific time
     *
     * response will not be executed by the MessageExecutor
     *
     * @param message message to send
     * @return response (can be null if timeout occurred)
     */
    public <ResponseType extends IMessage> ResponseType sendAndWait(IMessage message) throws TimeoutException {
        // create unique message id
        long id = messageID.incrementAndGet();

        // create task
        message.setId(id);
        Task task = new Task(message);
        tasks.put(id, task);

        // send message
        send(message);

        // wait for response
        if (task.await(1000)) {
            throw new TimeoutException("response not received in 1000ms");
        }

        // remove task and return response
        tasks.remove(id);
        return (ResponseType) task.getResponse();
    }

    @Override
    public void run() {
        LOGGER.log(Level.INFO, "Channel started");
        try {
            while(true) {

                IMessage response = read();

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
        LOGGER.log(Level.INFO, "Channel stopped");

        // signal all waiting threads to continue / end
        for (Task task : tasks.values()) {
            task.signal();
        }

        // logout user
        if (user != null) {
            user.setLoggedIn(false);
        }
    }

    /**
     * @return  returns the user associated with this channel.
     *          null if no user logged in via this channel
     */
    public User user() {
        return user;
    }

    /**
     * sets the user
     * @param user user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * stops the connection and closes the channel
     */
    public abstract void stop();
}
