package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterCommand extends Command {
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
            req.setAttribute("msg", msg);
            return forward;
        }

        if (password.equals("")){
            msg = "Password should not be empty";
            req.setAttribute("msg", msg);
            return forward;
        }

        if (!password.equals(confirm)){
            msg = "Confirm password incorrect";
            req.setAttribute("msg", msg);
            return forward;
        }



        forward = Path.PAGE__MAIN;
        return forward;
    }
}
