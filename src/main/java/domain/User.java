package domain;

/**
 * stores information of user
 */
public class User {
    private String username;

    public User(String username) {
        this.username = username;
    }

    public String username() {
        return username;
    }

}
