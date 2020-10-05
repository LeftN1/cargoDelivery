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

public class MakeOrderCommand extends Command {

    private static final Logger LOG = Logger.getLogger(MakeOrderCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        LOG.debug("MakeOrderCommand starts");
        CityDAO cityDAO = new CityDAO();
        OrderDAO orderDAO = new OrderDAO();

        String adress = req.getParameter("adress");
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
            return CommandContainer.get("account").execute(req,resp);
        }

        //Тут приходится искать по имени, потому что не получается неявно передать cityId при помощи <input> + <datalist>
        City currentCity = cityDAO.findCityByName(req.getParameter("current"));


        City destination = cityDAO.findCityByName(req.getParameter("cityInp"));

        volume = Calculations.getVolume(length, width, height);
        cost = Calculations.getCost(currentCity, destination, weight, volume);

        if (req.getParameter("calculate") != null) {
            LOG.debug("do cost calculation");
            req.setAttribute("lastCurrent", currentCity);
            req.setAttribute("destination", destination);
            req.setAttribute("adress", adress);
            req.setAttribute("cType", cType);
            req.setAttribute("weight", weight);
            req.setAttribute("length", length);
            req.setAttribute("width", width);
            req.setAttribute("height", height);

            req.setAttribute("cost", cost);

            LOG.debug("cost calculation finished.");
            LOG.debug("MakeOrderCommand finished");
            return CommandContainer.get("account").execute(req,resp);
        }

        LOG.debug("Make delivery order");

        Delivery delivery = new Delivery();
        delivery.setUser((User) req.getSession().getAttribute("user"));
        delivery.setOrigin(currentCity);
        delivery.setDestination(destination);
        delivery.setAdress(adress);
        delivery.setType(cType);
        delivery.setWeight(weight);
        delivery.setVolume(volume);
        delivery.setCost(cost);

        delivery.addStatus(DeliveryStatus.NEW, new Date());

        orderDAO.saveDelivery(delivery);

        LOG.debug("Delivery order saved");
        LOG.debug("MakeOrderCommand finished");

        return CommandContainer.get("account").execute(req,resp);
    }

}
