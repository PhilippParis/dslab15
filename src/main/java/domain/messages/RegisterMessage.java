package domain.messages;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * Created by phili on 10/20/15.
 */
public class RegisterMessage implements IMessage {
    private String address;

    public RegisterMessage(String address) {
        this.address = address;
    }

    @Override
    public void execute(IMessageExecutor executor) {
        executor.executeRegisterMessage(this);
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "RegisterMessage{" +
                "address='" + address + '\'' +
                '}';
    }
}
