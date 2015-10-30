package domain;

import channels.IChannel;

import java.net.InetSocketAddress;

/**
 * User class
 */
public class User {
    private String username;
    private IChannel channel;
    private boolean loggedIn = false;
    private InetSocketAddress privateAddress = null;
    private String password;

    public User(String username) {
        this.username = username;
    }

    /**
     * @return true if the user is logged in; false otherwise
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * sets the logged in status of the user
     * @param loggedIn logged in
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;

        if (!loggedIn) {
            privateAddress = null;
        }
    }

    /**
     * @return returns the username of the user
     */
    public String username() {
        return username;
    }

    /**
     * @return returns the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * sets the password of the user
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return  returns the private address of the user (used for private messages)
     *          can be null if no private address was registered
     */
    public InetSocketAddress getPrivateAddress() {
        return privateAddress;
    }

    /**
     * sets the private address of the user
     * @param host IP
     * @param port post
     */
    public void setPrivateAddress(String host, int port) {
        privateAddress = new InetSocketAddress(host, port);
    }

    /**
     * @return true if the user has registered a private message
     */
    public boolean privateAddressRegistered() {
        return privateAddress != null;
    }

    /**
     * @return  returns the channel of the user
     *          null if the user is not logged in
     */
    public IChannel getChannel() {
        return channel;
    }

    /**
     * sets the connection channel of the user
     * @param channel channel of the user
     */
    public void setChannel(IChannel channel) {
        this.channel = channel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return !(username != null ? !username.equals(user.username) : user.username != null);
    }
}
