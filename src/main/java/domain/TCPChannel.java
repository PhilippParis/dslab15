package domain;

import java.net.Socket;

/**
 * Created by phili on 10/20/15.
 */
public class TCPChannel implements IChannel {
    private Socket socket;

    public TCPChannel(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void send(IMessage message) {

    }

    @Override
    public void run() {

    }
}
