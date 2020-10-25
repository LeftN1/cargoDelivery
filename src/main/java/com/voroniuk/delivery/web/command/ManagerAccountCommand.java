package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.OrderDAO;
import com.voroniuk.delivery.db.entity.Delivery;
import com.voroniuk.delivery.db.entity.DeliveryStatus;
import com.voroniuk.delivery.utils.Utils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Manager account. List of deliveries with filters by way and status.
 *
 * @author M. Voroniuk
 */

public class ManagerAccountCommand extends Command {
    private static final Logger LOG = Logger.getLogger(ManagerAccountCommand.class);
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        LOG.debug("Command starts");
        String forward;
        OrderDAO orderDAO = new OrderDAO();

        String rStatus = req.getParameter("status");
        String rOrigin = req.getParameter("origin");
        String rDestination = req.getParameter("destination");


        DeliveryStatus status = (DeliveryStatus) req.getSession().getAttribute("status");
        if (status == null) {
            status = DeliveryStatus.NEW;
        }

        int originId;
        int destinationId;

        try {
            status = rStatus != null ? DeliveryStatus.getStatusById(Integer.parseInt(rStatus)) : status;
            originId = rOrigin != null ? Integer.parseInt(rOrigin) : (int) req.getSession().getAttribute("originId");
            destinationId = rDestination != null ? Integer.parseInt(rDestination) : (int) req.getSession().getAttribute("destinationId");
        } catch (NumberFormatException | NullPointerException e) {
            status = DeliveryStatus.NEW;
            originId = 0;
            destinationId = 0;
        }

        int pageNo;
        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) orderDAO.countDeliveries(status, 0, originId, destinationId, new Date(0), new Date(0)) / pageSize);

        pageNo = Utils.getPageNoFromRequest(req, "page", totalPages);

        List<Delivery> deliveries = orderDAO.findDeliveriesByStatusAndCityId(status, originId, destinationId, (pageNo - 1) * pageSize, pageSize);

        req.setAttribute("pageNo", pageNo);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("deliveries", deliveries);

        req.getSession().setAttribute("status", status);
        req.getSession().setAttribute("originId", originId);
        req.getSession().setAttribute("destinationId", destinationId);

        forward = Path.PAGE__MANAGER_ACCOUNT;

        LOG.debug("Command finished");
        return forward;
    }


}
