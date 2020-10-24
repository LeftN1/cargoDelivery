package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.dao.OrderDAO;
import com.voroniuk.delivery.db.dao.UserDAO;
import com.voroniuk.delivery.db.entity.*;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MakeOrderCommandTest {


    private static MakeOrderCommand makeOrderCommand = new MakeOrderCommand();
    private static HttpSession session = mock(HttpSession.class);
    private static HttpServletRequest request = mock(HttpServletRequest.class);
    private static HttpServletResponse response = mock(HttpServletResponse.class);
    private static UserDAO userDAO = new UserDAO();
    private static OrderDAO orderDAO = new OrderDAO();
    private static CityDAO cityDAO = new CityDAO();
    private static Locale en = SiteLocale.EN.getLocale();
    private static int originId;
    private static int destinationId;

    private static City origin;
    private static City destination;
    private static String address= "address111";

    @Before
    public void init() {
        User user = userDAO.findUserById(2);
        origin = cityDAO.findCityByName("Bar");
        destination = cityDAO.findCityByName("Nemyriv");

        when(session.getAttribute("locale")).thenReturn(en);
        when(session.getAttribute("user")).thenReturn(user);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("type")).thenReturn("1");
        when(request.getParameter("weight")).thenReturn("1");
        when(request.getParameter("length")).thenReturn("1");
        when(request.getParameter("width")).thenReturn("1");
        when(request.getParameter("height")).thenReturn("1");
        when(request.getParameter("current")).thenReturn(origin.getName(en));
        when(request.getParameter("cityInp")).thenReturn(destination.getName(en));
        when(request.getParameter("address")).thenReturn(address);


        originId = origin.getId();
        destinationId = destination.getId();
    }

    @Test
    public void shouldMakeOrder() throws IOException, ServletException {

        int oldCount = orderDAO.countDeliveries(DeliveryStatus.NEW, 0, originId, destinationId, new Date(0), new Date(0));

        String forward = makeOrderCommand.execute(request, response);

        int newCount = orderDAO.countDeliveries(DeliveryStatus.NEW, 0, originId, destinationId, new Date(0), new Date(0));

        assertTrue(newCount > oldCount);
        assertEquals(Path.COMMAND__USER_ACCOUNT, forward);

        List<Delivery> deliveries = orderDAO.findDeliveriesByStatusAndUserIdAndDate(DeliveryStatus.NEW, 0, originId, destinationId, new Date(0), new Date(0),0, newCount);
        Delivery last = new Delivery();
        last.setId(0);
        for (Delivery d : deliveries){
            if(d.getId() > last.getId()){
                last = d;
            }
        }
        orderDAO.deleteDeliveryById(last.getId());

    }

    @Test
    public void shouldCalculate() throws IOException, ServletException {
        int oldCount = orderDAO.countDeliveries(DeliveryStatus.NEW, 0, originId, destinationId, new Date(0), new Date(0));
        when(request.getParameter("calculate")).thenReturn("calculate");

        String forward = makeOrderCommand.execute(request, response);
        int newCount = orderDAO.countDeliveries(DeliveryStatus.NEW, 0, originId, destinationId, new Date(0), new Date(0));

        verify(request).setAttribute("lastCurrent", origin);
        verify(request).setAttribute("destination", destination);
        verify(request).setAttribute("address", address);
        verify(request).setAttribute("cType", CargoType.PARCEL);
        verify(request).setAttribute("weight", 1);
        verify(request).setAttribute("length", 1);
        verify(request).setAttribute("width", 1);
        verify(request).setAttribute("height", 1);
        verify(request).setAttribute("cost", 16);

        assertEquals(oldCount, newCount);
        assertEquals(Path.COMMAND__USER_ACCOUNT, forward);
    }

}