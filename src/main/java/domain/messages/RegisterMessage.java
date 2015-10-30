package domain.messages;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * !register <IP:port>
 * registers the address on the server
 */
public class RegisterMessage extends IMessage {
    private String host;
    private int port;

    public RegisterMessage(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void execute(IMessageExecutor executor) {
        executor.executeRegisterMessage(this);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "RegisterMessage{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
