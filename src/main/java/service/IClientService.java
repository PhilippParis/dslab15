package service;

import domain.IMessage;

/**
 * Created by phili on 10/29/15.
 */
public interface IClientService {

    /**
     * sets if the user is currently logged in
     * @param loggedIn logged in
     */
    void setLoggedIn(boolean loggedIn);

    /**
     * @return returns true if the client is currently logged in
     */
    boolean isLoggedIn();

    /**
     * sets username of the currently logged in User
     * @param username username
     */
    void setUsername(String username);

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
}
