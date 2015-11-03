package dao;

import domain.User;

import java.util.Collection;

/**
 * data access object for the chatserver users
 */
public interface IUserDAO {

    /**
     * @param username username of the user
     * @return  creates and returns the user with the specified username
     *          returns null if the user could not be found
     */
    User getUser(String username);

    /**
     * @return returns all registered users
     */
    Collection<User> getAllUsers();
}
