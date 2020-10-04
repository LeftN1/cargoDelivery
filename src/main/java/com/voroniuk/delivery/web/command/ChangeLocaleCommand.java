package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.entity.SiteLocales;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class ChangeLocaleCommand extends Command {

    private static final Logger LOG = Logger.getLogger(ChangeLocaleCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        LOG.debug("Locale changed to ");
        String forward = Path.PAGE__MAIN;

//        try {
//            forward = new URI(req.getHeader("referer")).getPath();
//        } catch (URISyntaxException e) {
//            LOG.warn(e.getStackTrace());
//        }

        Locale choosen = SiteLocales.valueOf(req.getParameter("choosenLang")).getLocale();

        req.getSession().setAttribute("language", choosen.getLanguage());

        return forward;
    }
}
