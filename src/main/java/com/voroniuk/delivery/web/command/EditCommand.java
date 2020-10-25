package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.OrderDAO;
import com.voroniuk.delivery.db.entity.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Command provides delivery edition
 *
 * @author M. Voroniuk
 */
public class EditCommand extends Command {
    private static final Logger LOG = Logger.getLogger(EditCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        LOG.debug("Command starts");

        OrderDAO orderDAO = new OrderDAO();

        String sId;
        sId = req.getParameter("delivery_id");
        if (sId == null) {
            sId = (String) req.getSession().getAttribute("s_delivery_id");
        }

        int id;

        try {
            id = Integer.parseInt(sId);
        } catch (NumberFormatException e) {
            return Path.COMMAND__ACCOUNT;
        }

        //save id to session on case change locale
        req.getSession().setAttribute("s_delivery_id", String.valueOf(id));

        Delivery delivery = orderDAO.findDeliveryById(id);

        City origin = delivery.getOrigin();
        City destination = delivery.getDestination();
        String adress = delivery.getAdress();
        CargoType cType = delivery.getType();
        int weight = delivery.getWeight();
        int volume = delivery.getVolume();
        int cost = (int) delivery.getCost();

        req.setAttribute("delivery_id", id);
        req.setAttribute("origin", origin);
        req.setAttribute("destination", destination);
        req.setAttribute("adress", adress);
        req.setAttribute("cType", cType);
        req.setAttribute("weight", weight);
        req.setAttribute("volume", volume);
        req.setAttribute("cost", cost);


        LOG.debug("Command finished");

        return Path.PAGE__EDIT;
    }
}
