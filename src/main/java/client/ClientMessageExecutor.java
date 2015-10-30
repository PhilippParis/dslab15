package client;

import channels.IChannel;
import cli.Shell;
import domain.messages.*;
import domain.responses.*;
import executors.IMessageExecutor;
import service.IClientService;
import service.IConnectionService;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Logger;

/**
 * Created by phili on 10/20/15.
 */
public class ClientMessageExecutor extends IMessageExecutor {
    private final static Logger LOGGER = Logger.getLogger(ClientMessageExecutor.class.getName());
    private Shell shell;
    private IConnectionService connectionService;
    private IClientService clientService;
    private IChannel channel;

    public ClientMessageExecutor(Shell shell, IConnectionService connectionService, IClientService clientService, IChannel channel) {
        this.shell = shell;
        this.connectionService = connectionService;
        this.clientService = clientService;
        this.channel = channel;
    }

    @Override
    public void executeSendMessage(SendMessage message) {
        LOGGER.info("message received: " + message);
        try {
            shell.writeLine(message.getText());
        } catch (IOException e) {
            LOGGER.info("displaying public message failed: " + message);
        }
        clientService.setLastMessage(message);
    }

    @Override
    public void executePrivateMessage(PrivateMessage message) {
        LOGGER.info("message received: " + message);

        try {
            shell.writeLine(message.getSender() + ": " + message.getText());
        } catch (IOException e) {
            LOGGER.info("displaying private message failed" + message);
        }

        AckResponse response = new AckResponse();
        response.setId(message.getId());

        // send response
        connectionService.send(response, channel);

        // stop getChannel
        connectionService.closeChannel(channel);
    }
}
