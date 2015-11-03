package channels;

import domain.IMessage;
import domain.Task;
import domain.responses.ErrorResponse;
import exceptions.InvalidMessageException;
import service.IConnectionService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Channel Interface
 * represents a connection which can be used to send and receive messages
 */
public abstract class IChannel implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(IChannel.class.getName());
    private AtomicLong messageID = new AtomicLong();
    private ConcurrentHashMap<Long, Task> tasks = new ConcurrentHashMap<Long, Task>();
    private ArrayList<OnCloseListener> listeners = new ArrayList<>();
    private IConnectionService connectionService;

    public IChannel(IConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    /**
     * sends a message via this getChannel
     * @param message message to send
     */
    public abstract void send(IMessage message);

    /**
     * blocks till a message was received and returns this message
     * @return returns the received message
     * @throws IOException  if an error reading the message occurred
     * @throws ClassNotFoundException if the type of the message is wrong
     */
    protected abstract IMessage read() throws IOException, InvalidMessageException;

    /**
     * sends the message and waits for a response
     * times out after a specific time
     *
     * response will not be executed by the MessageExecutor
     *
     * @param message message to send
     * @return response (can be null if timeout occurred)
     */
    public <T> T sendAndWait(IMessage message) throws TimeoutException, InvalidMessageException {
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

        try {
            return (T) task.getResponse();
        } catch (ClassCastException e) {
            LOGGER.log(Level.INFO, "invalid message received", e);
            throw new InvalidMessageException(e);
        }
    }

    /**
     * listens to incoming messages and wakes up the corresponding thread
     * if the message is a response to a message send with 'sendAndWait(IMessage)'.
     * otherwise the message will be executed by the message executor
     */
    @Override
    public void run() {
        LOGGER.log(Level.INFO, "Channel started");
        try {
            while(true) {
                try {
                    IMessage response = read();
                    if (tasks.containsKey(response.getId())) {
                        // if send and wait is active -> notify waiting thread
                        Task task = tasks.get(response.getId());
                        task.setResponse(response);
                        task.signal();
                    } else {
                        // send and wait not active -> use executor
                        connectionService.execute(response, this);
                    }
                } catch (InvalidMessageException e) {
                    // invalid message -> send error response
                    ErrorResponse response = new ErrorResponse("ERROR: invalid message received");
                    connectionService.send(response, this);
                }
            }
        } catch (IOException e) {
            // client closed connection -> stop listening
        }
        LOGGER.log(Level.INFO, "Channel stopped");

        // signal all waiting threads to continue / end
        for (Task task : tasks.values()) {
            task.signal();
        }

        // call listeners
        for (OnCloseListener listener : listeners) {
            listener.onClose(this);
        }
    }

    /**
     * adds a listener which will be called if this channel stops the execution
     * and closes the connection
     * @param listener listener
     */
    public void addOnCloseListener(OnCloseListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * stops the connection and closes the channel
     */
    public abstract void stop();
}
