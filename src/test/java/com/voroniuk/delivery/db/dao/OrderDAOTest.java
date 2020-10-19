package com.voroniuk.delivery.db.dao;

import com.voroniuk.delivery.db.entity.*;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class OrderDAOTest {
    private static CityDAO cityDAO;
    private static UserDAO userDAO;
    private static OrderDAO orderDAO;
    private static City origin;
    private static City destination;
    private static String address = "address";
    private static User user;

    private static Delivery delivery;

    @BeforeClass
    public static void init() {
        cityDAO = new CityDAO();
        userDAO = new UserDAO();
        orderDAO = new OrderDAO();
        origin = cityDAO.findCityById(1);
        destination = cityDAO.findCityById(2);
        user = userDAO.findUserById(1);

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
    }

    @AfterClass
    public static void cleanUp() {
        orderDAO.deleteDeliveryById(delivery.getId());
        assertNull(orderDAO.findDeliveryById(delivery.getId()));
    }

    @Test
    public void shouldChangeDeliverryId() {
        int id = delivery.getId();
        assertTrue(id > 0);
    }

    @Test
    public void shouldSaveAndFoundDelivery() {
        int id = delivery.getId();

        Delivery founded = orderDAO.findDeliveryById(id);

        assertEquals(delivery.getUser().getId(), founded.getUser().getId());
        assertEquals(delivery.getOrigin().getId(), founded.getOrigin().getId());
        assertEquals(delivery.getDestination().getId(), founded.getDestination().getId());
        assertEquals(delivery.getDestination().getId(), founded.getDestination().getId());
        assertEquals(delivery.getAdress(), founded.getAdress());
        assertEquals(delivery.getType(), founded.getType());
        assertEquals(delivery.getWeight(), founded.getWeight());
        assertEquals(delivery.getVolume(), founded.getVolume());
        assertEquals(delivery.getCost(), founded.getCost());
        assertEquals(delivery.getLastStatus(), founded.getLastStatus());
        assertEquals(delivery.getLastStatus(), founded.getLastStatus());
    }

    @Test
    public void shouldChangeCurrentStatus(){
        orderDAO.changeCurrentStatus(delivery, DeliveryStatus.PAID, new Date());
        int id = delivery.getId();
        Delivery founded = orderDAO.findDeliveryById(id);

        assertEquals(delivery.getLastStatus(), founded.getLastStatus());

    }

    @Test
    public void shouldFindDeliveriesByStatusAndUserIdAndDate(){
        int count = orderDAO.countDeliveries(delivery.getLastStatus(),
                delivery.getUser().getId(), delivery.getOrigin().getId(),
                delivery.getDestination().getId(), new Date(0), new Date(0));

        List<Delivery> deliveries = orderDAO.findDeliveriesByStatusAndUserIdAndDate(delivery.getLastStatus(),
                delivery.getUser().getId(), delivery.getOrigin().getId(), delivery.getDestination().getId(),
                new Date(0), new Date(0), 0, count);

        Delivery founded = new Delivery();

        for (Delivery d : deliveries){
            if(d.getId() == delivery.getId()){
                founded = d;
            }
        }

        assertTrue(deliveries.size()>0);
        assertNotNull(founded);

        assertEquals(delivery.getUser().getId(), founded.getUser().getId());
        assertEquals(delivery.getOrigin().getId(), founded.getOrigin().getId());
        assertEquals(delivery.getDestination().getId(), founded.getDestination().getId());
        assertEquals(delivery.getDestination().getId(), founded.getDestination().getId());
        assertEquals(delivery.getAdress(), founded.getAdress());
        assertEquals(delivery.getType(), founded.getType());
        assertEquals(delivery.getWeight(), founded.getWeight());
        assertEquals(delivery.getVolume(), founded.getVolume());
        assertEquals(delivery.getCost(), founded.getCost());
        assertEquals(delivery.getLastStatus(), founded.getLastStatus());

    }

    @Test
    public void shoulFoundInReport(){

        DeliveryStatus reportStatus = DeliveryStatus.NEW;

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        orderDAO.changeCurrentStatus(delivery, DeliveryStatus.PROCESSED, new Date());

        int count = orderDAO.countReportDeliveries(reportStatus,
                0, 0,
                0, new Date(0), new Date(0));

        List<Delivery> deliveries = orderDAO.reportDeliveriesByStatusAndUserIdAndDate(reportStatus,
                0, 0, 0,
                new Date(0), new Date(0), 0, count);

        Delivery founded = new Delivery();

        for (Delivery d : deliveries){
            if(d.getId() == delivery.getId()){
                founded = d;
            }
        }

        assertTrue(deliveries.size()>0);
        assertNotNull(founded);
        assertNotEquals(reportStatus, founded.getLastStatus());

    }


}