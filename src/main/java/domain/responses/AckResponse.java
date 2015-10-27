package domain.responses;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * Created by phili on 10/24/15.
 */
public class AckResponse extends IMessage {
    @Override
    public void execute(IMessageExecutor executor) {
        executor.executeAckResponse(this);
    }
}
