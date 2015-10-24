package domain.messages;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * Created by phili on 10/24/15.
 */
public class PrivateMessage extends IMessage {
    private String receiver;
    private String text;

    public PrivateMessage(String receiver, String text) {
        this.receiver = receiver;
        this.text = text;
    }

    @Override
    public void execute(IMessageExecutor executor) {
        executor.executePrivateMessage(this);
    }

    public String getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "PrivateMessage{" +
                "receiver='" + receiver + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
