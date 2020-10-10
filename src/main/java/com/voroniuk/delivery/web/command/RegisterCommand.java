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

public class RegisterCommand extends Command {

    private static final Logger LOG = Logger.getLogger(RegisterCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String msg="";
        String forward = Path.PAGE__REGISTER;

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");

        if (login == null && password == null) {
            return forward;
        }

        if (login.equals("")){
            msg = "Login should not be empty";
//            msg = "<fmt:message key=\"register.message.login_empty\"/>";
            req.setAttribute("msg", msg);
            return forward;
        }

        if (password.equals("")){
            msg = "Password should not be empty";
//            msg = "<fmt:message key=\"register.message.password_empty\"/>";
            req.setAttribute("msg", msg);
            return forward;
        }

        if (!password.equals(confirm)){
            msg = "Confirm password incorrect";
//            msg = "<fmt:message key=\"register.message.confirm_incorrect\"/>";
            req.setAttribute("msg", msg);
            return forward;
        }

        UserDAO userDAO = new UserDAO();

        if(userDAO.findUserByLogin(login)!=null){
//            msg = "<fmt:message key=\"register.message.user_with_login\"/>" + login + "<fmt:message key=\"register.message.already_exists\"/>";
            msg = "User with login '" + login + "' already exists\"/>";
            req.setAttribute("msg", msg);
            return forward;
        }

        User newUser = new User();

        newUser.setLogin(login);
        newUser.setPassword(DigestUtils.md5Hex(password));
        newUser.setRole(Role.USER);

        userDAO.saveUser(newUser);
        req.getSession().setAttribute("user", newUser);

        //PRG pattern
        String redirect = Path.COMMAND__ACCOUNT;
        resp.setStatus(resp.SC_MOVED_PERMANENTLY);
        resp.setHeader("Location", redirect);
        LOG.debug("Redirect to :" + redirect);


        return redirect;
    }
}
