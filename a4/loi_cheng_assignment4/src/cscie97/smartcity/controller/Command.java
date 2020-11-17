package cscie97.smartcity.controller;

import cscie97.smartcity.authenticator.AuthException;
import cscie97.smartcity.model.ServiceException;

/**
 * The command interface, each concrete class has very specific actions to perform
 * uses System.out.println to show changes made to devices
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public interface Command {

    /**
     * Execute, performs all the actions from the command
     *
     * @throws ServiceException if command cannot execute
     */
    void execute() throws ServiceException, AuthException;

}
