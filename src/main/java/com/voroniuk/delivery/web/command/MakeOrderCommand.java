package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.dao.OrderDAO;
import com.voroniuk.delivery.db.entity.*;
import com.voroniuk.delivery.utils.Calculations;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class MakeOrderCommand extends Command {

    private static final Logger LOG = Logger.getLogger(MakeOrderCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        LOG.debug("MakeOrderCommand starts");
        CityDAO cityDAO = new CityDAO();
        OrderDAO orderDAO = new OrderDAO();

        Locale locale = (Locale) req.getSession().getAttribute("locale");
        if (locale == null) {
            locale = Locale.getDefault();
        }

        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        CargoType cType = CargoType.getTypeById(Integer.parseInt(req.getParameter("type")));

        int weight;
        int length;
        int width;
        int height;
        int volume;
        int cost;

        try {
            weight = Integer.parseInt(req.getParameter("weight"));
            length = Integer.parseInt(req.getParameter("length"));
            width = Integer.parseInt(req.getParameter("width"));
            height = Integer.parseInt(req.getParameter("height"));
        } catch (NumberFormatException e) {
            return Path.COMMAND__ACCOUNT;
        }


        City currentCity = cityDAO.findCityByName(req.getParameter("current"));
        if (currentCity == null) {
            req.setAttribute("origin_city_msg", rb.getString("user.error.message.origin_not_valid"));
            return Path.COMMAND__ACCOUNT;
        }

        City destination = cityDAO.findCityByName(req.getParameter("cityInp"));
        if (destination == null) {
            req.setAttribute("destination_city_msg", rb.getString("user.error.message.destination_not_valid"));
            return Path.COMMAND__ACCOUNT;
        }

        String address = req.getParameter("address");
        if(address == null){
            req.setAttribute("address_msg", rb.getString("user.error.message.address_is_empty"));
            return Path.COMMAND__ACCOUNT;
        }

        volume = Calculations.getVolume(length, width, height);
        cost = Calculations.getCost(currentCity, destination, weight, volume);

        if (req.getParameter("calculate") != null) {
            LOG.debug("do cost calculation");
            req.setAttribute("lastCurrent", currentCity);
            req.setAttribute("destination", destination);
            req.setAttribute("address", address);
            req.setAttribute("cType", cType);
            req.setAttribute("weight", weight);
            req.setAttribute("length", length);
            req.setAttribute("width", width);
            req.setAttribute("height", height);
            req.setAttribute("cost", cost);
            LOG.debug("cost calculation finished.");
            LOG.debug("MakeOrderCommand finished");
            return Path.COMMAND__ACCOUNT;
        }

        LOG.debug("Make delivery order");

        Delivery delivery = new Delivery();
        delivery.setUser((User) req.getSession().getAttribute("user"));
        delivery.setOrigin(currentCity);
        delivery.setDestination(destination);
        delivery.setAddress(address);
        delivery.setType(cType);
        delivery.setWeight(weight);
        delivery.setVolume(volume);
        delivery.setCost(cost);
        delivery.addStatus(DeliveryStatus.NEW, new Date());

        orderDAO.saveDelivery(delivery);

        LOG.debug("Delivery order saved");
        LOG.debug("MakeOrderCommand finished");
        //PRG pattern
        String redirect = Path.COMMAND__ACCOUNT;
        resp.setStatus(resp.SC_MOVED_PERMANENTLY);
        resp.setHeader("Location", redirect);
        LOG.debug("Redirect to :" + redirect);

        return Path.COMMAND__ACCOUNT;
    }

}
