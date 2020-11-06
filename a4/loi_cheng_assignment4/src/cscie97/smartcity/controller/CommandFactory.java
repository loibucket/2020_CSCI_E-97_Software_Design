package cscie97.smartcity.controller;

import cscie97.smartcity.controller.Command;
import cscie97.smartcity.model.ServiceException;

/**
 * The command factory interface allows creation of commands
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public interface CommandFactory {

    /**
     * Create a command, to be executed later
     *
     * @return a command object
     * @throws ServiceException if error in creating object
     */
    Command createCommand() throws ServiceException;

}
