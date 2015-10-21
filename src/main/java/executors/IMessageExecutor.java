package executors;

import domain.messages.*;
import domain.responses.*;

/**
 * Created by phili on 10/20/15.
 */
public abstract class IMessageExecutor {

    public void executeLoginMessage(LoginMessage message) {
        // ignore message
    }

    public void executeLogoutMessage(LogoutMessage message) {
        // ignore message
    }

    public void executeSendMessage(SendMessage message) {
        // ignore message
    }

    public void executeRegisterMessage(RegisterMessage message) {
        // ignore message
    }

    public void executeLookupMessage(LookupMessage message) {
        // ignore message
    }

    public void executeListMessage(ListMessage message) {
        // ignore message
    }

    public void executeLoginResponse(LoginResponse message) {
        // ignore message
    }

    public void executeLogoutResponse(LogoutResponse message) {
        // ignore message
    }

    public void executeSendResponse(SendResponse message) {
        // ignore message
    }

    public void executeRegisterResponse(RegisterResponse message) {
        // ignore message
    }

    public void executeLookupResponse(LookupResponse message) {
        // ignore message
    }

    public void executeListResponse(ListResponse message) {
        // ignore message
    }
}
