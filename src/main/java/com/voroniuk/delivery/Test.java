package com.voroniuk.delivery;

import com.voroniuk.delivery.dao.DBManager;
import com.voroniuk.delivery.dao.DBManagerImpl;
import com.voroniuk.delivery.entity.City;

import java.util.LinkedList;
import java.util.List;

public class Test {
    public static void main(String[] args) {

        DBManager manager = DBManagerImpl.getInstance();

        City a = manager.getCity("������");
        //City b = manager.getCity("����");
        City b = manager.getCity("�����");
        //City b = manager.getCity("�������");

        System.out.printf("distance between %s and %s  =  %.2f km\n",a.getName(), b.getName(), manager.findDistance(a, b));

        List<City> allCities = manager.findAllCities();

    }
}
