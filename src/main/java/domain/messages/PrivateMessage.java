package domain.messages;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * Created by phili on 10/24/15.
 */
public class PrivateMessage extends IMessage {
    private String receiver;
    private String sender;
    private String text;

    public PrivateMessage(String receiver, String sender, String text) {
        this.receiver = receiver;
        this.sender = sender;
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

    public String getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "PrivateMessage{" +
                "receiver='" + receiver + '\'' +
                ", sender='" + sender + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
