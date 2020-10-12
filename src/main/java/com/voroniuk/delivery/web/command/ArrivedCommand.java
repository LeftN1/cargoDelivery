package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.OrderDAO;
import com.voroniuk.delivery.db.entity.Delivery;
import com.voroniuk.delivery.db.entity.DeliveryStatus;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class ArrivedCommand extends Command {
    private static final Logger LOG = Logger.getLogger(ArrivedCommand.class);
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        LOG.debug("Command starts");

        OrderDAO orderDAO = new OrderDAO();

        String sId = req.getParameter("delivery_id");
        int id;

        try{
            id = Integer.parseInt(sId);
        }catch (NumberFormatException e){
            return Path.COMMAND__ACCOUNT;
        }

        Delivery delivery = orderDAO.findDeliveryById(id);

        orderDAO.changeCurrentStatus(delivery, DeliveryStatus.ARRIVED, new Date());

        String redirect = Path.COMMAND__ACCOUNT;
        resp.setStatus(resp.SC_MOVED_PERMANENTLY);
        resp.setHeader("Location", redirect);
        LOG.debug("Redirect to :" + redirect);

        return Path.COMMAND__ACCOUNT;
    }
}
