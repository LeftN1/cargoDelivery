package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.OrderDAO;
import com.voroniuk.delivery.db.entity.Delivery;
import com.voroniuk.delivery.db.entity.DeliveryStatus;
import com.voroniuk.delivery.db.entity.Role;
import com.voroniuk.delivery.db.entity.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AccountCommand extends Command {

    private static final Logger LOG = Logger.getLogger(AccountCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {



        String forward = Path.PAGE__ERROR_PAGE;
        User user = (User) req.getSession().getAttribute("user");

        if(user.getRole()== Role.USER) {
//            String redirect = Path.COMMAND__USER_ACCOUNT;
//            resp.setStatus(resp.SC_TEMPORARY_REDIRECT);
//            resp.setHeader("Location", redirect);
//            LOG.debug("Redirect to :" + redirect);
            forward = Path.COMMAND__USER_ACCOUNT;
        }

        if(user.getRole()== Role.MANAGER) {
//            String redirect = Path.COMMAND__MANAGER_ACCOUNT;
//            resp.setStatus(resp.SC_TEMPORARY_REDIRECT);
//            resp.setHeader("Location", redirect);
//            LOG.debug("Redirect to :" + redirect);
            forward = Path.COMMAND__MANAGER_ACCOUNT;
        }

        return forward;
    }
}
