package service;

import channels.IChannel;
import dao.IUserDAO;
import domain.User;
import util.Config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the UserService interface
 */
public class UserService implements IUserService {
    private Map<String, User> users = new ConcurrentHashMap<>();
    private IUserDAO userDAO;

    public UserService(IUserDAO userDAO) {
        this.userDAO = userDAO;

        // add all users
        for (User user : userDAO.getAllUsers()) {
            addUser(user);
        }
    }

    @Override
    public boolean addUser(User user) {
        if (user == null || users.containsKey(user.username())) {
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
            if (user.getChannel() != null && user.getChannel().equals(channel)) {
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

        synchronized (user) {
            boolean success = user.getPassword().equals(password);

            if (success) {
                users.put(user.username(), user);
                user.setLoggedIn(true);
            }
            return success;
        }
    }

    @Override
    public void logoutUser(User user) {
        if (user != null) {
            synchronized (user) {
                user.setLoggedIn(false);
            }
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }
}
