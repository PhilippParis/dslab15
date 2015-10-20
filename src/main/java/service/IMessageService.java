package service;

import domain.IMessage;

import java.io.IOException;

/**
 * Created by phili on 10/20/15.
 */
public interface IMessageService {

    /**
     * executes the message
     * @param message message to execute
     */
    void execute(IMessage message);

    /**
     * serializes and encodes the message to a byte array for sending
     * @param message message to serialize/encode
     * @return serialized message
     */
    byte[] encode(IMessage message) throws IOException;

    /**
     * decodes the data and returns the message object
     * @param data data sent over the network
     * @return the message object
     */
    IMessage decode(byte[] data) throws IOException, ClassNotFoundException;
}
