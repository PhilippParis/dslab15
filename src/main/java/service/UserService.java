package service;

import channels.IChannel;
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

    public User getUser(IChannel channel) {
        for (User user : users.values()) {
            if (user.channel().equals(channel)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    public boolean loginUser(User user, String password) {
        if (user == null || password == null) {
            return false;
        }

        boolean success = false;

        try {
            success = loginConfig.getString(user.username() + ".password").equals(password);
        } catch (MissingResourceException e) {
            return false;
        }

        if (success) {
            users.put(user.username(), user);
            user.setLoggedIn(true);
        }
        return success;
    }

    @Override
    public void logoutUser(User user) {
        user.setLoggedIn(false);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }
}
