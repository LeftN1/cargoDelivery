package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Logout command
 *
 * @author M. Voroniuk
 */

public class LogoutCommand extends Command {
    private static final Logger LOG = Logger.getLogger(LogoutCommand.class);
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String forward = Path.COMMAND__MAIN;

        req.getSession().setAttribute("user", null);
        LOG.debug("Logged out");

        return forward;

    }
}
