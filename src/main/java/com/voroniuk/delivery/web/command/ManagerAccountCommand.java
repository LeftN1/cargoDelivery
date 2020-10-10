package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.OrderDAO;
import com.voroniuk.delivery.db.entity.City;
import com.voroniuk.delivery.db.entity.Delivery;
import com.voroniuk.delivery.db.entity.DeliveryStatus;
import com.voroniuk.delivery.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ManagerAccountCommand extends Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String forward;
        OrderDAO orderDAO = new OrderDAO();

        String rStatus = req.getParameter("status");
        String rOrigin = req.getParameter("origin");
        String rDestination = req.getParameter("destination");



        DeliveryStatus status = (DeliveryStatus) req.getSession().getAttribute("status");
        if(status == null){
            status = DeliveryStatus.NEW;
        }
        City origin = new City();
        City destination = new City();

        try {
            status = rStatus != null ? DeliveryStatus.getStatusById(Integer.parseInt(rStatus)) : status;
        } catch (NumberFormatException e) {
            status = DeliveryStatus.NEW;
        }

        int pageNo;
        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) orderDAO.countDeliveriesByStatus(status) / pageSize);

        pageNo = Utils.getPageNoFromRequest(req, "page", totalPages);

        List<Delivery> deliveries = orderDAO.findDeliveriesByStatus(status, (pageNo - 1) * pageSize, pageSize);

        req.setAttribute("pageNo", pageNo);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("deliveries", deliveries);

        req.getSession().setAttribute("status", status);
        req.getSession().setAttribute("origin", origin);
        req.getSession().setAttribute("destination", destination);


        forward = Path.PAGE__MANAGER_ACCOUNT;

        return forward;
    }



}
