package domain;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by phili on 10/26/15.
 */
public class Task {
    private IMessage message = null;
    private IMessage response = null;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public Task(IMessage message) {
        this.message = message;
    }

    public IMessage getResponse() {
        return response;
    }

    public void setResponse(IMessage response) {
        this.response = response;
    }

    public Long getId() {
        return this.message.getId();
    }

    /**
     *
     * @param timeout
     * @return timedout
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

        } finally {
            lock.unlock();
        }

        return false;
    }

    public void signal() {
        lock.lock();
        condition.signal();
        lock.unlock();
    }
}
