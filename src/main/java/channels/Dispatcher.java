package channels;

import service.IChannelService;
import service.IMessageService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Created by phili on 10/20/15.
 */
public class Dispatcher implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(Dispatcher.class.getName());
    private ServerSocket serverSocket;
    private IMessageService messageService;
    private IChannelService channelService;

    public Dispatcher(IChannelService channelService, IMessageService messageService, ServerSocket serverSocket) {
        this.channelService = channelService;
        this.messageService = messageService;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run()  {
        LOGGER.info("Dispatcher started");
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                channelService.addChannel(createTCPChannel(socket));
            }
        } catch (IOException e) { }
        LOGGER.info("Dispatcher stopped");
    }

    public IChannel createTCPChannel(Socket socket) {
        return new TCPChannel(socket, messageService);
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}