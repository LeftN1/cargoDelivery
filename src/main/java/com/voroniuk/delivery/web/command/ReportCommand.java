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
import java.util.concurrent.TimeUnit;

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
        String sStart = (String) req.getSession().getAttribute("start");
        String sEnd = (String) req.getSession().getAttribute("end");

        int originId;
        int destinationId;

        Date startDate;
        Date endDate;

        City origin;
        City destination;

        long oneDay = 24 * 60 * 60 * 1000;

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
            startDate = rStart != null ? format.parse(rStart) : format.parse(sStart);
            endDate = rEnd != null ? format.parse(rEnd) : format.parse(sEnd);
        } catch (ParseException | NullPointerException e) {
            endDate = new Date();
            startDate = new Date(endDate.getTime()/oneDay * oneDay);
        }


        long days = TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS);

        int pageNo;
        int totalPages = (int) days + 1;
        pageNo = Utils.getPageNoFromRequest(req, "page", totalPages);

        Date sD = new Date(startDate.getTime() + (pageNo - 1) * oneDay);
        Date eD = new Date(sD.getTime() + oneDay);
        int pageSize = orderDAO.countDeliveries(status, 0, originId, destinationId, sD, eD);

        List<Delivery> deliveries = orderDAO.findDeliveriesByStatusAndCityIdAndDate(status, originId, destinationId, sD, eD, 0, pageSize);

        long totalWeight = 0;
        long totalVolume = 0;
        long totalCost = 0;

        for (Delivery d : deliveries) {
            totalWeight += d.getWeight();
            totalVolume += d.getVolume();
            totalCost += d.getCost();
        }

        req.setAttribute("pageNo", pageNo);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("deliveries", deliveries);
        req.setAttribute("currentDate", format.format(startDate.getTime() + (pageNo - 1) * oneDay));
        req.setAttribute("totalWeight", totalWeight);
        req.setAttribute("totalVolume", totalVolume);
        req.setAttribute("totalCost", totalCost);

        req.getSession().setAttribute("status", status);
        req.getSession().setAttribute("originId", originId);
        req.getSession().setAttribute("destinationId", destinationId);
        req.getSession().setAttribute("start", format.format(startDate));
        req.getSession().setAttribute("end", format.format(endDate));

        return Path.PAGE__REPORT;
    }
}
