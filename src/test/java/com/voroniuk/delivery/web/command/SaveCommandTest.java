package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.dao.OrderDAO;
import com.voroniuk.delivery.db.dao.UserDAO;
import com.voroniuk.delivery.db.entity.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SaveCommandTest {

    private static CityDAO cityDAO;
    private static UserDAO userDAO;
    private static OrderDAO orderDAO;
    private static City origin;
    private static City destination;
    private static String address = "address";
    private static User user;
    private static Locale locale = Locale.getDefault();

    private static Delivery delivery;

    private static String newAddress = "new";
    private static int newWeight = 7;
    private static int newVolume = 8;
    private static int newCost = 9;
    private static City newOrigin;
    private static City newDestination;

    private static HttpServletRequest request = mock(HttpServletRequest.class);
    private static HttpServletResponse response = mock(HttpServletResponse.class);

    String ok = Path.COMMAND__ACCOUNT;
    String error = Path.PAGE__ERROR_PAGE;
    String main = Path.COMMAND__MAIN;

    @BeforeClass
    public static void init() {
        cityDAO = new CityDAO();
        userDAO = new UserDAO();
        orderDAO = new OrderDAO();
        origin = cityDAO.findCityById(1);
        destination = cityDAO.findCityById(2);
        user = userDAO.findUserById(2);

        delivery = new Delivery();
        delivery.setUser(user);
        delivery.setOrigin(origin);
        delivery.setDestination(destination);
        delivery.setAddress(address);
        delivery.setType(CargoType.CARGO);
        delivery.setWeight(1);
        delivery.setVolume(1);
        delivery.setCost(1);
        delivery.addStatus(DeliveryStatus.NEW, new Date());
        orderDAO.saveDelivery(delivery);

        newOrigin = cityDAO.findCityById(10);
        newDestination = cityDAO.findCityById(11);

        when(request.getParameter("adress")).thenReturn(newAddress);
        when(request.getParameter("delivery_id")).thenReturn(String.valueOf(delivery.getId()));
        when(request.getParameter("weight")).thenReturn(String.valueOf(newWeight));
        when(request.getParameter("volume")).thenReturn(String.valueOf(newVolume));
        when(request.getParameter("cost")).thenReturn(String.valueOf(newCost));
        when(request.getParameter("origin")).thenReturn(newOrigin.getName(locale));
        when(request.getParameter("destination")).thenReturn(newDestination.getName(locale));
        when(request.getParameter("type")).thenReturn("1");
    }

    @AfterClass
    public static void cleanup() {
        orderDAO.deleteDeliveryById(delivery.getId());
    }

    @Test
    public void shouldUpdateDelivery() {

        SaveCommand saveCommand = new SaveCommand();
        try {
            saveCommand.execute(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }

        Delivery founded = orderDAO.findDeliveryById(delivery.getId());

        assertEquals(newAddress, founded.getAdress());
        assertEquals(newWeight, founded.getWeight());
        assertEquals(newVolume, founded.getVolume());
        assertEquals(newCost, founded.getCost());
        assertEquals(newOrigin.getId(), founded.getOrigin().getId());
        assertEquals(newDestination.getId(), founded.getDestination().getId());
    }

    @Test
    public void shouldReturnAccount(){

        SaveCommand saveCommand = new SaveCommand();
        String forward = "";
        try {
            forward = saveCommand.execute(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }

        assertEquals(ok, forward);

    }

}