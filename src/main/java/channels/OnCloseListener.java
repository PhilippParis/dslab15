package channels;

/**
 * Listener for the close event
 */
public interface OnCloseListener {
    /**
     * called if the channel stops listening and closes the connection
     * @param channel channel which closes
     */
    void onClose(IChannel channel);
}
