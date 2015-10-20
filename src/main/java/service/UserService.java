package service;

import domain.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the UserService interface
 */
public class UserService implements IUserService {
    private Map<String, User> users = new HashMap<>();

    @Override
    public boolean addUser(User user) {
        if (users.containsKey(user.username())) {
            return false;
        }

        users.put(user.username(), user);
        return true;
    }

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }
}
