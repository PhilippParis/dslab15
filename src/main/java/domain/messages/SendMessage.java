package domain.messages;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * Created by phili on 10/20/15.
 */
public class SendMessage extends IMessage {
    private String text;

    public SendMessage(String text) {
        this.text = text;
    }

    @Override
    public void execute(IMessageExecutor executor) {
        executor.executeSendMessage(this);
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "SendMessage{" +
                "text='" + text + '\'' +
                '}';
    }
}
