package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.entity.SiteLocale;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

public class ChangeLocaleCommand extends Command {

    private static final Logger LOG = Logger.getLogger(ChangeLocaleCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {


        String from = req.getParameter("from");
        if(from.equals("")){
            from = "main";
        }

        String forward = "/controller?command="+from;

        Locale choosen = SiteLocale.valueOf(req.getParameter("choosenLang")).getLocale();
        req.getSession().setAttribute("locale", choosen);

        LOG.debug("Locale changed to " + choosen);
        return forward;
    }
}
