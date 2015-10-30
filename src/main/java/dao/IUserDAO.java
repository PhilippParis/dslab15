package dao;

import domain.User;

import java.util.Collection;

/**
 * Created by phili on 10/30/15.
 */
public interface IUserDAO {

    User getUser(String username);

    Collection<User> getAllUsers();
}
