package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.dao.OrderDAO;
import com.voroniuk.delivery.db.entity.City;
import com.voroniuk.delivery.db.entity.Delivery;
import com.voroniuk.delivery.db.entity.DeliveryStatus;
import com.voroniuk.delivery.db.entity.Total;
import com.voroniuk.delivery.utils.Utils;
import org.apache.log4j.Logger;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ReportCommand extends Command {
    private static final Logger LOG = Logger.getLogger(ReportCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        LOG.debug("Command starts");

        String sessionId = req.getSession().getId();



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

        Locale rLocale = (Locale) req.getSession().getAttribute("locale");
        if(rLocale == null){
            rLocale = Locale.getDefault();
        }

        final Locale locale = rLocale;

        String type = req.getParameter("type");
        if (type == null) {
            String sType = (String) req.getSession().getAttribute("type");
            type = type == null ? "by_date" : sType;
        }

        int originId;
        int destinationId;

        Date startDate;
        Date endDate;

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
            endDate.setTime(endDate.getTime() + oneDay - 1);
        } catch (ParseException | NullPointerException e) {
            startDate = new Date(0);
            endDate = new Date(0);
        }


        int pageNo;
        int pageSize = 10;
        int totalPages = 10;
        pageNo = Utils.getPageNoFromRequest(req, "page", totalPages);

        Map<String, List<Delivery>> report = new LinkedHashMap<>();
        Map<String, Total> totals = new LinkedHashMap<>();

        SimpleDateFormat repFormat = new SimpleDateFormat("dd.MM.yyyy");

        int totalDel = orderDAO.countReportDeliveries(status, 0, originId, destinationId, startDate, endDate);
        List<Delivery> deliveries = orderDAO.reportDeliveriesByStatusAndCityIdAndDate(status, originId, destinationId, startDate, endDate, 0, totalDel);

        if (type.equals("by_date")) {
            final DeliveryStatus fStatus = status;

            Comparator<Delivery> statusDateComparator = Comparator.comparing(d -> d.getStatusDate(fStatus));

            deliveries = deliveries.stream().sorted(statusDateComparator).collect(Collectors.toList());

            if (deliveries.size() > 0) {
                Date currDate = new Date(deliveries.get(0).getStatusDate(status).getTime() / oneDay * oneDay);
                List<Delivery> currList = new LinkedList();
                Total total = new Total();
                for (Delivery delivery : deliveries) {
                    if (delivery.getStatusDate(status).getTime() > currDate.getTime() + oneDay) {
                        report.put(repFormat.format(currDate), currList);
                        totals.put(repFormat.format(currDate), total);
                        currDate.setTime(delivery.getStatusDate(status).getTime() / oneDay * oneDay);
                        currList = new LinkedList<>();
                        total = new Total();
                    }
                    currList.add(delivery);
                    total.addWeight(delivery.getWeight());
                    total.addVolume(delivery.getVolume());
                    total.addCost(delivery.getCost());
                }
                totals.put(repFormat.format(currDate), total);
                report.put(repFormat.format(currDate), currList);
            }

        }

        if (type.equals("by_city")) {
            if (originId == 0){
                originId = 266;
            }
            deliveries = deliveries.stream().sorted(Comparator.comparing(o -> o.getDestination().getName(locale))).collect(Collectors.toList());

            if (deliveries.size() > 0) {
                City currCity = deliveries.get(0).getDestination();
                List<Delivery> currList = new LinkedList();
                Total total = new Total();
                for (Delivery delivery : deliveries) {
                    if (delivery.getDestination().getId() != currCity.getId()) {
                        report.put(currCity.getName(locale), currList);
                        totals.put(currCity.getName(locale), total);
                        currCity = delivery.getDestination();
                        currList = new LinkedList<>();
                        total = new Total();
                    }
                    currList.add(delivery);
                    total.addWeight(delivery.getWeight());
                    total.addVolume(delivery.getVolume());
                    total.addCost(delivery.getCost());
                }
                totals.put(currCity.getName(locale), total);
                report.put(currCity.getName(locale), currList);
            }
        }

        req.setAttribute("pageNo", pageNo);
        req.setAttribute("totalPages", totalPages);
//        req.setAttribute("report", report);
//        req.setAttribute("totals", totals);
//        req.setAttribute("reportName", reportName);

        req.getSession().setAttribute("report", report);
        req.getSession().setAttribute("totals", totals);

        req.getSession().setAttribute("type", type);
        req.getSession().setAttribute("status", status);
        req.getSession().setAttribute("originId", originId);
        req.getSession().setAttribute("destinationId", destinationId);
        req.getSession().setAttribute("start", startDate.getTime() != 0 ? format.format(startDate) : null);
        req.getSession().setAttribute("end", endDate.getTime() != 0 ? format.format(endDate) : null);

        return Path.PAGE__REPORT;
    }





}
