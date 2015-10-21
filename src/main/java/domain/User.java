package domain;

/**
 * stores information of user
 */
public class User {
    private String username;
    private IChannel channel;
    private boolean loggedIn = false;

    public User(String username, IChannel channel) {
        this.username = username;
        this.channel = channel;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String username() {
        return username;
    }

    public IChannel channel() {
        return channel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return !(username != null ? !username.equals(user.username) : user.username != null);
    }
}
