package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.UserDAO;
import com.voroniuk.delivery.db.entity.Role;
import com.voroniuk.delivery.db.entity.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginCommand extends Command {
    private static final Logger LOG = Logger.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        LOG.debug("Command starts");

        UserDAO userDAO = new UserDAO();

        Locale locale = (Locale) req.getSession().getAttribute("locale");
        if (locale == null) {
            locale = Locale.getDefault();
        }
        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        String login = req.getParameter("login");
        LOG.debug("request param: login: " + login);



        String password = req.getParameter("password");
        String passHash = DigestUtils.md5Hex(password);

        String forward = Path.PAGE__ERROR_PAGE;
        String errorMessage;

        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            errorMessage = rb.getString("login.message.login_pass_cant_be_empty");
            req.setAttribute("msg", errorMessage);
            LOG.warn("errorMessage --> " + errorMessage);
            forward = Path.COMMAND__MAIN;
            return forward;
        }

        User user = userDAO.findUserByLogin(login);
        LOG.trace("Found user: " + user);


        if (user != null && user.getPassword().equals(passHash)) {
            Role userRole = user.getRole();
            LOG.trace("User role: " + userRole);

            forward = Path.COMMAND__ACCOUNT;

            req.getSession().setAttribute("user", user);
            LOG.trace("Set session attribute: user = " + user);

            LOG.info("User " + user + " logged in as " + user.getRole());


        } else {
            errorMessage = rb.getString("login.message.login_pass_incorrect");
            req.setAttribute("msg", errorMessage);
            forward = Path.COMMAND__MAIN;
        }

        LOG.debug("Command finished, forward to: " + forward);
        return forward;
    }
}
