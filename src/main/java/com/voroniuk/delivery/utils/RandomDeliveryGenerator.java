package com.voroniuk.delivery.utils;

import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.dao.OrderDAO;
import com.voroniuk.delivery.db.dao.UserDAO;
import com.voroniuk.delivery.db.entity.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RandomDeliveryGenerator {

    public static void main(String[] args) {

        generate(10);

    }

    public static void generate(int count){

        OrderDAO orderDAO = new OrderDAO();
        UserDAO userDAO = new UserDAO();
        CityDAO cityDAO = new CityDAO();

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date dMin = new Date(0);
        Date dMax = new Date(0);

        try {
            dMin = format.parse("10.09.2020");
            dMax = format.parse("10.10.2020");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long lMin = dMin.getTime();
        long lMax = dMax.getTime();
        long delta = lMax - lMin;

        Delivery delivery;

        City origin = new City();
        City destination = new City();
        String address = "adress";

        User user = userDAO.findUserById(2);

        for (int i =0; i< count; i++) {

            int iO = (int) (Math.random()*400+1);
            int iD = (int) (Math.random()*400+1);
            long rand = (long) (Math.random() * delta + lMin);

            origin = cityDAO.findCityById(iO);
            destination = cityDAO.findCityById(iD);

            delivery = new Delivery();
            delivery.setUser(user);
            delivery.setOrigin(origin);
            delivery.setDestination(destination);
            delivery.setAddress(address);
            delivery.setType(CargoType.CARGO);
            delivery.setWeight(1);
            delivery.setVolume(1);
            delivery.setCost(1);
            delivery.addStatus(DeliveryStatus.NEW, new Date(rand));
            orderDAO.saveDelivery(delivery);
        }

    }

}
