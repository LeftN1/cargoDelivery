package com.voroniuk.delivery.db.dao;

import com.voroniuk.delivery.db.entity.City;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class CityDAOTest {

    CityDAO cityDAO = new CityDAO();
    String odessa = "Odessa";
    String kyiv = "Киев";
    int id = 266;

    @Test
    public void findCityByName() {

        City city = cityDAO.findCityByName(odessa);

        assertEquals(odessa, city.getName(Locale.US));
    }

    @Test
    public void findCityById() {

        City city = cityDAO.findCityById(id);

        assertEquals(id, city.getId());
    }

    @Test
    public void findAllCities() {

        List citylist = cityDAO.findAllCities();

        assertTrue(citylist.size() > 0);
        assertTrue(citylist.get(0) != null);
    }

    @Test
    public void findDistance() {

        City city1 = cityDAO.findCityByName(odessa);
        City city2 = cityDAO.findCityByName(kyiv);

        assertTrue(cityDAO.findDistance(city1, city2) > 0);
    }
}