package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.OrderDAO;
import com.voroniuk.delivery.db.entity.Delivery;
import com.voroniuk.delivery.db.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserAccountCommand extends Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String forward = Path.PAGE__ERROR_PAGE;
        OrderDAO orderDAO = new OrderDAO();

        User user = (User) req.getSession().getAttribute("user");

        List<Delivery> deliveries = orderDAO.findUserDeliveries(user);
        req.setAttribute("deliveries", deliveries);
        forward = Path.PAGE__USER_ACCOUNT;

        return forward;
    }
}
