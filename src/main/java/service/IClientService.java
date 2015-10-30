package service;

import channels.Dispatcher;
import domain.IMessage;

/**
 * ClientService interface
 */
public interface IClientService {

    /**
     * sets the status to logged out
     */
    void logout();

    /**
     * @return returns true if the client is currently logged in
     */
    boolean isLoggedIn();

    /**
     * sets the user status to logged in and sets the current username
     * @param username username
     */
    void login(String username);

    /**
     * @return  returns the username of the currently logged in account
     *          returns null if the user is not logged in
     */
    String username();

    /**
     * @return returns the last publicly received message
     */
    IMessage lastMessage();

    /**
     * sets the last publicly received message
     * @param message message
     */
    void setLastMessage(IMessage message);

    /**
     * creates and adds a dispatcher for incomming private connections
     * @param port port
     */
    Dispatcher createPrivateConnectionDispatcher(int port);
}
