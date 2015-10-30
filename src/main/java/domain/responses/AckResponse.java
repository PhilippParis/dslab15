package domain.responses;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * Acknowledgement for the reception of a private message
 */
public class AckResponse extends IMessage {
    @Override
    public void execute(IMessageExecutor executor) {
        executor.executeAckResponse(this);
    }
}
