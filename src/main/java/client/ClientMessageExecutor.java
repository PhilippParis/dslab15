package client;

import channels.IChannel;
import domain.messages.*;
import domain.responses.*;
import executors.IMessageExecutor;
import service.IConnectionService;

import java.io.PrintStream;
import java.util.logging.Logger;

/**
 * Created by phili on 10/20/15.
 */
public class ClientMessageExecutor extends IMessageExecutor {
    private final static Logger LOGGER = Logger.getLogger(ClientMessageExecutor.class.getName());
    private PrintStream userResponseStream;
    private IConnectionService connectionService;
    private IChannel channel;

    public ClientMessageExecutor(PrintStream userResponseStream, IConnectionService connectionService, IChannel channel) {
        this.userResponseStream = userResponseStream;
        this.connectionService = connectionService;
        this.channel = channel;
    }

    @Override
    public void executeSendMessage(SendMessage message) {
        LOGGER.info("message received: " + message.toString());
        userResponseStream.println(message.getText());
    }

    @Override
    public void executePrivateMessage(PrivateMessage message) {
        LOGGER.info("message received: " + message.toString());
        userResponseStream.println(message.getText());

        AckResponse response = new AckResponse();
        response.setId(message.getId());

        // send response
        connectionService.send(response, channel);

        // stop channel
        connectionService.closeChannel(channel);
    }
}
