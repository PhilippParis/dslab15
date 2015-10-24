package domain.responses;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * Created by phili on 10/21/15.
 */
public class LookupResponse extends IMessage {
    private String host;
    private int port;
    private boolean successful;

    @Override
    public void execute(IMessageExecutor executor) {
        executor.executeLookupResponse(this);
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

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    @Override
    public String toString() {
        return "LookupResponse{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", successful=" + successful +
                '}';
    }
}
