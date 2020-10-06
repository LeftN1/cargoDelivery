package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.UserDAO;
import com.voroniuk.delivery.db.entity.Role;
import com.voroniuk.delivery.db.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginCommand extends Command{
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        UserDAO userDAO = new UserDAO();
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String passHash = DigestUtils.md5Hex(password);

        User user = userDAO.findUserByLogin(login);

        String forward = Path.PAGE__MAIN;

        if(user != null && user.getPassword().equals(passHash)){
            req.getSession().setAttribute("user", user);
            if(user.getRole() == Role.USER) {
                forward = Path.COMMAND__USER_ACCOUNT;
            }

            if(user.getRole() == Role.MANAGER){
                forward = Path.COMMAND__MANAGER_ACCOUNT;
            }

        }else {
            req.setAttribute("msg", "login or password incorrect");
            forward = Path.PAGE__MAIN;
        }

        return forward;
    }
}
