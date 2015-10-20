package service;

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
     * @param   username username of the user
     * @return  the user with the given username or null
     *          if no user with this username was found
     */
    User getUser(String username);

    /**
     * @return  all users
     */
    Collection<User> getAllUsers();
}
