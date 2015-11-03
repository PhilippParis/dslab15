package executors;

import channels.IChannel;
import domain.IMessage;
import domain.messages.*;
import domain.responses.*;
import service.IConnectionService;

import java.util.logging.Logger;

/**
 * MessageExecutor interface
 */
public abstract class IMessageExecutor {
    private final static Logger LOGGER = Logger.getLogger(IMessageExecutor.class.getName());
    private IChannel channel;
    private IConnectionService connectionService;

    public IMessageExecutor(IChannel channel, IConnectionService connectionService) {
        this.channel = channel;
        this.connectionService = connectionService;
    }

    public void executeLoginMessage(LoginMessage message) {
        sendErrorResponse(message);
    }

    public void executeLogoutMessage(LogoutMessage message) {
        sendErrorResponse(message);
    }

    public void executeSendMessage(SendMessage message) {
        sendErrorResponse(message);
    }

    public void executeRegisterMessage(RegisterMessage message) {
        sendErrorResponse(message);
    }

    public void executeLookupMessage(LookupMessage message) {
        sendErrorResponse(message);
    }

    public void executeListMessage(ListMessage message) {
        sendErrorResponse(message);
    }

    public void executePrivateMessage(PrivateMessage message) {
        sendErrorResponse(message);
    }

    public void executeLoginResponse(LoginResponse message) {
        sendErrorResponse(message);
    }

    public void executeLogoutResponse(LogoutResponse message) {
        sendErrorResponse(message);
    }

    public void executeSendResponse(SendResponse message) {
        sendErrorResponse(message);
    }

    public void executeRegisterResponse(RegisterResponse message) {
        sendErrorResponse(message);
    }

    public void executeLookupResponse(LookupResponse message) {
        sendErrorResponse(message);
    }

    public void executeListResponse(ListResponse message) {
        sendErrorResponse(message);
    }

    public void executeAckResponse(AckResponse message) {
        sendErrorResponse(message);
    }

    public void executeErrorResponse(ErrorResponse message) {
        // ignore message
    }

    private void sendErrorResponse(IMessage message) {
        LOGGER.severe("message type not expected: " + message);
        ErrorResponse response = new ErrorResponse("ERROR: message type not expected");
        response.setId(message.getId());
        connectionService.send(response, channel);
    }
}
