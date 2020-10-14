package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.dao.OrderDAO;
import com.voroniuk.delivery.db.entity.City;
import com.voroniuk.delivery.db.entity.Delivery;
import com.voroniuk.delivery.db.entity.DeliveryStatus;
import com.voroniuk.delivery.utils.Utils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportCommand extends Command {
    private static final Logger LOG = Logger.getLogger(ReportCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        LOG.debug("Command starts");

        OrderDAO orderDAO = new OrderDAO();
        CityDAO cityDAO = new CityDAO();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String rStatus = req.getParameter("status");
        String rOrigin = req.getParameter("origin");
        String rDestination = req.getParameter("destination");
        String rStart = req.getParameter("start_date");
        String rEnd = req.getParameter("end_date");

        int originId;
        int destinationId;

        Date startDate;
        Date endDate;

        City origin;
        City destination;

        DeliveryStatus status = (DeliveryStatus) req.getSession().getAttribute("status");
        if (status == null) {
            status = DeliveryStatus.NEW;
        }

        try {
            status = rStatus != null ? DeliveryStatus.getStatusById(Integer.parseInt(rStatus)) : status;
            originId = rOrigin != null ? Integer.parseInt(rOrigin) : (int) req.getSession().getAttribute("originId");
            destinationId = rDestination != null ? Integer.parseInt(rDestination) : (int) req.getSession().getAttribute("destinationId");
        } catch (NumberFormatException | NullPointerException e) {
            status = DeliveryStatus.NEW;
            originId = 0;
            destinationId = 0;
        }

        try {
            startDate = format.parse(rStart);
            endDate = format.parse(rEnd);
        } catch (ParseException | NullPointerException e) {
            startDate = new Date(0);
            endDate = new Date(0);
        }

        int pageNo;
        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) orderDAO.countDeliveries(status, 0, originId, destinationId, startDate, endDate) / pageSize);

        pageNo = Utils.getPageNoFromRequest(req, "page", totalPages);


        List<Delivery> deliveries = orderDAO.findDeliveriesByStatusAndCityIdAndDate(status, originId, destinationId, startDate, endDate, (pageNo - 1) * pageSize, pageSize);

        req.setAttribute("pageNo", pageNo);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("deliveries", deliveries);

        req.getSession().setAttribute("status", status);
        req.getSession().setAttribute("originId", originId);
        req.getSession().setAttribute("destinationId", destinationId);


        return Path.PAGE__REPORT;
    }
}
