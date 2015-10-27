package service;

import channels.IChannel;
import domain.User;

import java.util.Collection;

/**
 * service class for managing users
 */
public interface IUserService {

    /**
     * adds a user if no other user with the same username exists
     * @param   user user to add
     * @return  true if the user was successfully added
     */
    boolean addUser(User user);

    /**
     * removes a user
     * @param   user user to remove
     * @return  true if the user was successfully removed;
     *          false if the user did not exist
     */
    boolean removeUser(User user);

    /**
     * @param   username username of the user
     * @return  the user with the given username or null
     *          if no user with this username was found
     */
    User getUser(String username);

    /**
     * @param channel connection channel
     * @return returns the user for the specified channel
     */
    public User getUser(IChannel channel);

    /**
     * @return  all users
     */
    Collection<User> getAllUsers();

    /**
     * @param user user to authenticate
     * @param key authentication key
     * @return true if the user was successfully authenticated
     */
    boolean authenticate(User user, String key);
}
