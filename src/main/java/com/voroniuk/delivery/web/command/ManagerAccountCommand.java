package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ManagerAccountCommand extends Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String forward = Path.PAGE__MANAGER_ACCOUNT;

        return forward;
    }
}
