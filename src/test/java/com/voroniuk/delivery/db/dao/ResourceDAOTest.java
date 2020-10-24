package com.voroniuk.delivery.db.dao;

import com.voroniuk.delivery.db.entity.CargoType;
import com.voroniuk.delivery.db.entity.DeliveryStatus;
import com.voroniuk.delivery.db.entity.SiteLocale;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

public class ResourceDAOTest {

    private static ResourceDAO resourceDAO = new ResourceDAO();
    private static String enSomeString = "someString";
    private static String ruSomeString = "любаяСтрока";
    private static int resId = 0;
    private static Locale ru = SiteLocale.RU.getLocale();
    private static Locale en = SiteLocale.EN.getLocale();
    private static Locale uk = SiteLocale.UA.getLocale();

    @BeforeClass
    public static void before() throws SQLException {
        resId = resourceDAO.addResource();
        resourceDAO.addTranslation(resId, en, enSomeString);
        resourceDAO.addTranslation(resId, ru, ruSomeString);
    }

    @AfterClass
    public static void cleanUp(){
        resourceDAO.deleteResource(resId);
    }

    @Test
    public void shouldAddResource() throws SQLException {
        assertTrue(resId > 0);
    }

    @Test
    public void shouldFindIdByTranslation(){
        assertEquals(resId, resourceDAO.getResourceIdByTranslation(enSomeString));
        assertEquals(resId, resourceDAO.getResourceIdByTranslation(ruSomeString));
    }

    @Test
    public void shouldFindTranslationsById(){
        Map<Locale, String> translations = new HashMap<>();
        translations = resourceDAO.getTranslations(resId);
        assertEquals(enSomeString, translations.get(en));
        assertEquals(ruSomeString, translations.get(ru));
    }

    @Test
    public void shouldReturnLocaleById() throws SQLException {
        assertTrue(resourceDAO.getLocaleById(1)!=null);
        assertEquals(Locale.class, resourceDAO.getLocaleById(1).getClass());
    }

    @Test
    public void shouldReturnLocaleId() throws SQLException {
        assertTrue(resourceDAO.getLocaleId(en) > 0);
        assertTrue(resourceDAO.getLocaleId(ru) > 0);
        assertNotEquals(resourceDAO.getLocaleId(en), resourceDAO.getLocaleId(ru));
    }

    @Test
    public void shouldDeleteResource() throws SQLException {
        int id = resourceDAO.addResource();
        resourceDAO.addTranslation(id, en, enSomeString);
        resourceDAO.deleteResource(id);
        assertTrue(resourceDAO.getTranslations(id).isEmpty());
    }

    @Test
    public void shouldLoadStatuses(){

        for (DeliveryStatus status : DeliveryStatus.values()){
            assertEquals(status.getName(en), status.getName(ru));
        }

        resourceDAO.loadStatuses();
        for (DeliveryStatus status : DeliveryStatus.values()){
            String name = status.getName(en);
            int id = resourceDAO.getResourceIdByTranslation(name);

            assertNotEquals(status.getName(en), status.getName(ru));
            assertNotEquals(status.getName(en), status.getName(uk));

            assertEquals(status.getName(ru), resourceDAO.getTranslations(id).get(ru));
            assertEquals(status.getName(uk), resourceDAO.getTranslations(id).get(uk));
        }
    }

    @Test
    public void shouldLoadCargoTypes(){

        for(CargoType type : CargoType.values()){
            assertEquals(type.getName(en), type.getName(ru));
        }

        resourceDAO.loadCargoTypes();
        for(CargoType type : CargoType.values()){
            String name = type.getName(en);
            int id= resourceDAO.getResourceIdByTranslation(name);

            assertNotEquals(type.getName(en), type.getName(ru));
            assertNotEquals(type.getName(en), type.getName(uk));

            assertEquals(type.getName(ru), resourceDAO.getTranslations(id).get(ru));
            assertEquals(type.getName(uk), resourceDAO.getTranslations(id).get(uk));
        }
    }

}