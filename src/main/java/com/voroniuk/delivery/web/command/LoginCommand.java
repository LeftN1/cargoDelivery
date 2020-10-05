package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.UserDAO;
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

        String forward;

        if(user != null && user.getPassword().equals(passHash)){
            req.getSession().setAttribute("user", user);
            forward= CommandContainer.get("account").execute(req,resp);
        }else {
            req.setAttribute("msg", "login or password incorrect");
            forward = Path.PAGE__MAIN;
        }

        return forward;
    }
}
