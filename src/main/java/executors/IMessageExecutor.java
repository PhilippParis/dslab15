package executors;

import domain.IChannel;
import domain.IMessage;
import domain.User;

/**
 * Created by phili on 10/20/15.
 */
public interface IMessageExecutor {

    void executeLoginMessage(IMessage message);

    void executeLogoutMessage(IMessage message);

    void executeSendMessage(IMessage message);

    void executeRegisterMessage(IMessage message);

    void executeLookupMessage(IMessage message);

    void executeListMessage(IMessage message);
}
