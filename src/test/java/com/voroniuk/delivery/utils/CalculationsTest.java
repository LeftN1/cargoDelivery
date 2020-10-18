package com.voroniuk.delivery.utils;


import static org.junit.Assert.*;

import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.entity.City;
import org.junit.Test;

public class CalculationsTest {

    @Test
    public void shouldGetCostDependOnDistanceAndSize() {

        CityDAO cityDAO = new CityDAO();

        City origin = cityDAO.findCityByName("Odessa");

        City nik = cityDAO.findCityByName("Mykolayiv");
        City kyiv = cityDAO.findCityByName("Kyiv");

        int weight1 = 10;
        int weight2 = 20;

        int vol1 = 10;
        int vol2 = 20;

        double dist1 = cityDAO.findDistance(origin, nik);
        double dist2 = cityDAO.findDistance(origin, kyiv);

        assertTrue(dist2 > dist1);

        int cost1 = Calculations.getCost(origin, nik, weight1, vol1);
        int cost2 = Calculations.getCost(origin, nik, weight1, vol1);

        assertTrue(cost2 == cost1);

        cost1 = Calculations.getCost(origin, nik, weight1, vol1);
        cost2 = Calculations.getCost(origin, kyiv, weight1, vol1);

        assertTrue(cost2 > cost1);

        cost1 = Calculations.getCost(origin, nik, weight1, vol1);
        cost2 = Calculations.getCost(origin, nik, weight2, vol1);

        assertTrue(cost2 > cost1);

        cost1 = Calculations.getCost(origin, nik, weight1, vol1);
        cost2 = Calculations.getCost(origin, nik, weight1, vol2);

        assertTrue(cost2 > cost1);
    }

    @Test
    public void shouldGetVolume() {
        assertEquals(27, Calculations.getVolume(30, 30, 30));
    }
}