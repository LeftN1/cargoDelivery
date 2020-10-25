package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.dao.OrderDAO;
import com.voroniuk.delivery.db.entity.CargoType;
import com.voroniuk.delivery.db.entity.City;
import com.voroniuk.delivery.db.entity.Delivery;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Save command. Saves edited delivery from edit page.
 *
 * @author M. Voroniuk
 */

public class SaveCommand extends Command {
    private static final Logger LOG = Logger.getLogger(SaveCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        LOG.debug("Command starts");
        CityDAO cityDAO = new CityDAO();
        OrderDAO orderDAO = new OrderDAO();

        int id;

        int weight;
        int volume;
        int cost;

        String adress = req.getParameter("adress");

        try {
            id = Integer.parseInt(req.getParameter("delivery_id"));
            weight = Integer.parseInt(req.getParameter("weight"));
            volume = Integer.parseInt(req.getParameter("volume"));
            cost = Integer.parseInt(req.getParameter("cost"));
        } catch (NumberFormatException e) {
            return Path.COMMAND__EDIT;
        }

        City origin = cityDAO.findCityByName(req.getParameter("origin"));
        if(origin == null){
            return Path.COMMAND__EDIT;
        }

        City destination = cityDAO.findCityByName(req.getParameter("destination"));
        if(destination == null){
            return Path.COMMAND__EDIT;
        }

        CargoType cType = CargoType.getTypeById(Integer.parseInt(req.getParameter("type")));

        Delivery delivery = orderDAO.findDeliveryById(id);

        delivery.setWeight(weight);
        delivery.setVolume(volume);
        delivery.setCost(cost);
        delivery.setOrigin(origin);
        delivery.setDestination(destination);
        delivery.setAddress(adress);
        delivery.setType(cType);

        orderDAO.updateDelivery(delivery);

        LOG.debug("Delivery order saved");
        LOG.debug("Command finished");
        //PRG pattern
        String redirect = Path.COMMAND__ACCOUNT;
        resp.setStatus(resp.SC_TEMPORARY_REDIRECT);
        resp.setHeader("Location", redirect);
        LOG.debug("Redirect to :" + redirect);

        return Path.COMMAND__ACCOUNT;
    }
}
