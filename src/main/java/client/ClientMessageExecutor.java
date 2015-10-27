package client;

import domain.IChannel;
import domain.messages.*;
import domain.responses.*;
import executors.IMessageExecutor;
import service.IChannelService;

import java.io.PrintStream;
import java.util.logging.Logger;

/**
 * Created by phili on 10/20/15.
 */
public class ClientMessageExecutor extends IMessageExecutor {
    private final static Logger LOGGER = Logger.getLogger(ClientMessageExecutor.class.getName());
    private PrintStream userResponseStream;
    private IChannelService channelService;
    private IChannel channel;

    public ClientMessageExecutor(PrintStream userResponseStream, IChannelService channelService, IChannel channel) {
        this.userResponseStream = userResponseStream;
        this.channelService = channelService;
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
        channel.send(response);

        // stop channel
        channelService.closeChannel(channel);
    }
}
