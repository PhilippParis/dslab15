package executors;

import domain.IChannel;
import domain.IMessage;
import domain.User;
import domain.messages.*;

/**
 * Created by phili on 10/20/15.
 */
public interface IMessageExecutor {

    void executeLoginMessage(LoginMessage message);

    void executeLogoutMessage(LogoutMessage message);

    void executeSendMessage(SendMessage message);

    void executeRegisterMessage(RegisterMessage message);

    void executeLookupMessage(LookupMessage message);

    void executeListMessage(ListMessage message);
}
