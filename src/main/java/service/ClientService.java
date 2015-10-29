package service;

import domain.IMessage;

/**
 * Created by phili on 10/29/15.
 */
public class ClientService implements IClientService {
    private boolean loggedIn = false;
    private IMessage lastMessage;
    private String username;

    @Override
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public IMessage lastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(IMessage message) {
        this.lastMessage = message;
    }
}
