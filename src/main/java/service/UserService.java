package service;

import domain.User;
import util.Config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

/**
 * Implementation of the UserService interface
 */
public class UserService implements IUserService {
    private Map<String, User> users = new HashMap<>();
    private Config loginConfig;

    public UserService(Config loginConfig) {
        this.loginConfig = loginConfig;
    }

    @Override
    public boolean addUser(User user) {
        if (users.containsKey(user.username())) {
            return false;
        }

        users.put(user.username(), user);
        return true;
    }

    @Override
    public boolean removeUser(User user) {
        if (users.containsKey(user.username())) {
            users.remove(user.username());
            return true;
        }

        return false;
    }

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    public boolean authenticate(User user, String password) {
        if (user == null || password == null) {
            return false;
        }

        try {
            return loginConfig.getString(user.username() + ".password").equals(password);
        } catch (MissingResourceException e) {
            // wrong username
            return false;
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }
}
