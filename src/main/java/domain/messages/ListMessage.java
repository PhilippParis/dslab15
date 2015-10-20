package domain.messages;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * Created by phili on 10/20/15.
 */
public class ListMessage implements IMessage {

    public ListMessage() {
    }

    @Override
    public void execute(IMessageExecutor executor) {
        executor.executeListMessage(this);
    }

    @Override
    public String toString() {
        return "ListMessage{}";
    }
}
