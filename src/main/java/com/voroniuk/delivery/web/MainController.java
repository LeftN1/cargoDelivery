package com.voroniuk.delivery.web;


import com.voroniuk.delivery.db.entity.SiteLocales;
import com.voroniuk.delivery.web.command.Command;
import com.voroniuk.delivery.web.command.CommandContainer;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@WebServlet("/")
public class MainController extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(MainController.class);

    @Override
    public void init() throws ServletException {
        super.init();
        getServletContext().setAttribute("locales", SiteLocales.values());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       process(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        LOG.debug("MainController starts");

        String commandName = req.getParameter("command");

        Command command = CommandContainer.get(commandName);

        String forward = command.execute(req, resp);

        LOG.debug("Controller finished. forward adress: " + forward);

        if (forward != null) {
            req.getRequestDispatcher(forward).forward(req, resp);
        }
    }

}
