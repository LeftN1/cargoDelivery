package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.dao.OrderDAO;
import com.voroniuk.delivery.db.dao.UserDAO;
import com.voroniuk.delivery.db.entity.*;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MakeBillCommandTest {

    private OrderDAO orderDAO = new OrderDAO();
    private CityDAO cityDAO = new CityDAO();
    private UserDAO userDAO = new UserDAO();

    private MakeBillCommand makeBillCommand = new MakeBillCommand();
    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    public void shouldChangeStatusToArrived() throws IOException, ServletException {

        City origin = cityDAO.findCityByName("Bar");
        City destination = cityDAO.findCityByName("Nemyriv");
        User user = userDAO.findUserById(2);
        String address = "address";
        int originId = origin.getId();
        int destinationId = destination.getId();

        Delivery delivery = new Delivery();
        delivery.setUser(user);
        delivery.setOrigin(origin);
        delivery.setDestination(destination);
        delivery.setAddress(address);
        delivery.setType(CargoType.PARCEL);
        delivery.setWeight(77);
        delivery.setVolume(77);
        delivery.setCost(77);
        delivery.addStatus(DeliveryStatus.NEW, new Date());
        orderDAO.saveDelivery(delivery);

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        when(request.getParameter("delivery_id")).thenReturn(String.valueOf(delivery.getId()));

        makeBillCommand.execute(request, response);

        Delivery founded = orderDAO.findDeliveryById(delivery.getId());

        assertEquals(DeliveryStatus.PROCESSED, founded.getLastStatus());

        orderDAO.deleteDeliveryById(delivery.getId());

    }

}