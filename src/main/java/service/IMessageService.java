package service;

import domain.IMessage;

/**
 * Created by phili on 10/20/15.
 */
public interface IMessageService {

    /**
     * @param message
     */
    void execute(IMessage message);
}
