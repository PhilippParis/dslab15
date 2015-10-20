package domain.messages;

import domain.IMessage;
import executors.IMessageExecutor;

/**
 * Created by phili on 10/20/15.
 */
public class LoginMessage implements IMessage {
    private String username;
    private String password;

    public LoginMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void execute(IMessageExecutor executor) {
        executor.executeLoginMessage(this);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "LoginMessage{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
