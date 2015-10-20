package domain;

import service.IChannelService;

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
    private IChannelService channelService;

    public Dispatcher(IChannelService channelService, ServerSocket serverSocket) {
        this.channelService = channelService;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run()  {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                channelService.addChannel(createTCPChannel(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    IChannel createTCPChannel(Socket socket) {
        return new TCPChannel(socket);
    }
}
