package com.voroniuk.delivery.db.dao;

import com.voroniuk.delivery.db.entity.City;
import com.voroniuk.delivery.db.entity.Country;
import com.voroniuk.delivery.db.entity.Region;
import com.voroniuk.delivery.db.entity.SiteLocale;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class CityDAOTest {

    private Locale en = SiteLocale.EN.getLocale();
    private Locale ru = SiteLocale.RU.getLocale();
    private Locale uk = SiteLocale.UA.getLocale();

    private ResourceDAO resourceDAO = new ResourceDAO();
    private CityDAO cityDAO = new CityDAO();
    private String odessa = "Odessa";
    private String kyiv = "Киев";
    private int id = 266;

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

    @Test
    public void shouldAddAndDeleteCountry() {
        Country country = new Country();
        int id;
        String enName = "Some";
        String ruName = "Любой";
        String ukName = "БудьЯкий";

        country.addName(en, enName);
        country.addName(ru, ruName);
        country.addName(uk, ukName);

        cityDAO.addCountry(country);
        id = country.getId();

        assertTrue(country.getId() > 0);
        assertTrue(country.getNameResourceId() > 0);
        assertNotEquals(-1, resourceDAO.getResourceIdByTranslation(enName));
        assertNotEquals(-1, resourceDAO.getResourceIdByTranslation(ruName));
        assertNotEquals(-1, resourceDAO.getResourceIdByTranslation(ukName));

        cityDAO.deleteCountry(country);

        assertNull(cityDAO.findCountryById(id));
        assertEquals(-1, resourceDAO.getResourceIdByTranslation(enName));
        assertEquals(-1, resourceDAO.getResourceIdByTranslation(ruName));
        assertEquals(-1, resourceDAO.getResourceIdByTranslation(ukName));
    }

    @Test
    public void shouldAddAndDeleteRegion() {
        Region region = new Region();
        int id;
        String enName = "Some";
        String ruName = "Любой";
        String ukName = "БудьЯкий";

        region.addName(en, enName);
        region.addName(ru, ruName);
        region.addName(uk, ukName);
        region.setCountryId(1);

        cityDAO.addRegion(region);
        id = region.getId();

        assertTrue(region.getId() > 0);
        assertTrue(region.getNameResourceId() > 0);
        assertNotEquals(-1, resourceDAO.getResourceIdByTranslation(enName));
        assertNotEquals(-1, resourceDAO.getResourceIdByTranslation(ruName));
        assertNotEquals(-1, resourceDAO.getResourceIdByTranslation(ukName));

        cityDAO.deleteRegion(region);

        assertNull(cityDAO.findRegionById(id));
        assertEquals(-1, resourceDAO.getResourceIdByTranslation(enName));
        assertEquals(-1, resourceDAO.getResourceIdByTranslation(ruName));
        assertEquals(-1, resourceDAO.getResourceIdByTranslation(ukName));
    }

    @Test
    public void shouldAddAndDeleteCity() {
        City city = new City();
        int id;
        String enName = "Some";
        String ruName = "Любой";
        String ukName = "БудьЯкий";

        city.addName(en, enName);
        city.addName(ru, ruName);
        city.addName(uk, ukName);
        city.setRegionId(1);

        cityDAO.addCity(city);
        id = city.getId();

        assertTrue(city.getId() > 0);
        assertTrue(city.getNameResourceId() > 0);
        assertNotEquals(-1, resourceDAO.getResourceIdByTranslation(enName));
        assertNotEquals(-1, resourceDAO.getResourceIdByTranslation(ruName));
        assertNotEquals(-1, resourceDAO.getResourceIdByTranslation(ukName));

        cityDAO.deleteCity(city);

        assertNull(cityDAO.findRegionById(id));
        assertEquals(-1, resourceDAO.getResourceIdByTranslation(enName));
        assertEquals(-1, resourceDAO.getResourceIdByTranslation(ruName));
        assertEquals(-1, resourceDAO.getResourceIdByTranslation(ukName));
    }

    @Test
    public void shouldFindCitiesByRegionId() {
        List<City> cities = cityDAO.findCitiesByRegionId(1);
        assertTrue(!cities.isEmpty());
        assertTrue(cities.get(0) != null);
    }

    @Test
    public void findAllRegions() {

        List regions = cityDAO.findAllRegions();

        assertTrue(!regions.isEmpty());
        assertTrue(regions.get(0) != null);
    }


}