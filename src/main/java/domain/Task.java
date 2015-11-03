package domain;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SendAndWait - Task
 */
public class Task {
    private final static Logger LOGGER = Logger.getLogger(Task.class.getName());
    private IMessage message = null;
    private IMessage response = null;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public Task(IMessage message) {
        this.message = message;
    }

    /**
     * @return  returns the response message;
     *          can be null, if no response was received
     */
    public IMessage getResponse() {
        return response;
    }

    /**
     * sets the response message
     * @param response response to the message
     */
    public void setResponse(IMessage response) {
        this.response = response;
    }

    /**
     * stops the current thread until a response is received
     * or the timeout occurred
     * @param timeout timeout in milliseconds
     * @return timedout true if the timeout period elapsed before a response was received
     */
    public boolean await(long timeout) {
        lock.lock();
        boolean inTime = true;
        try {
            while(response == null) {
                if (!inTime) {
                    return true;
                }
                inTime = condition.await(timeout, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.INFO, "task interrupted", e);
        } finally {
            lock.unlock();
        }

        return false;
    }

    /**
     * signals the waiting thread to continue
     */
    public void signal() {
        lock.lock();
        condition.signal();
        lock.unlock();
    }
}
