package dao;

import domain.User;
import util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.MissingResourceException;

/**
 * Created by phili on 10/30/15.
 */
public class ConfigUserDAO implements IUserDAO {
    private Config config;

    public ConfigUserDAO(Config config) {
        this.config = config;
    }

    @Override
    public User getUser(String username) {
        try {
            User user = new User(username);
            user.setPassword(config.getString(user.username() + ".password"));
            return user;
        } catch (MissingResourceException e) {
            return null;
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        for (String key : config.listKeys()) {
            String username = key.substring(0, key.length() - 9);
            users.add(getUser(username));
        }

        return users;
    }
}
