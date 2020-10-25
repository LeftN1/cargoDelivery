package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.entity.Role;
import com.voroniuk.delivery.db.entity.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Command return account page according to user role
 *
 * @author M. Voroniuk
 */

public class AccountCommand extends Command {

    private static final Logger LOG = Logger.getLogger(AccountCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        LOG.debug("Command starts");

        String forward = Path.PAGE__ERROR_PAGE;
        User user = (User) req.getSession().getAttribute("user");

        if (user == null) {
            LOG.debug("User is empty");
            return forward;
        }

        LOG.debug("user role is " + user.getRole());
        if (user.getRole() == Role.USER) {

            forward = Path.COMMAND__USER_ACCOUNT;
        }

        if (user.getRole() == Role.MANAGER) {

            forward = Path.COMMAND__MANAGER_ACCOUNT;
        }

        LOG.debug("Command finished");
        LOG.debug("Forward to " + forward);
        return forward;
    }
}
