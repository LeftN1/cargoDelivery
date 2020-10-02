package com.voroniuk.delivery.db.dao;

import com.voroniuk.delivery.db.entity.City;
import com.voroniuk.delivery.db.entity.Country;
import com.voroniuk.delivery.db.entity.Region;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;


public class CityDAO {

    private static final Logger LOG = Logger.getLogger(CityDAO.class);

    private ResourceDAO resourceDAO;

    public CityDAO() {
        this.resourceDAO = resourceDAO = new ResourceDAO();
    }

    public void addCountry(Country country) {

        for (Locale locale : country.getNames().keySet()) {
            if (resourceDAO.getResourceIdByTranslation(country.getName(locale))>0) {
                throw new IllegalArgumentException("Country " + country.getName(locale) + " already exists");
            }
        }

        String sql = "INSERT INTO countries (name_resource_id) VALUE (?)";
        int resId;

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            resId = resourceDAO.addResource();
            statement.setInt(1, resId);

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    country.setId(resultSet.getInt(1));
                }
            }

            for (Locale locale : country.getNames().keySet()) {
                resourceDAO.addTranslation(resId, locale, country.getName(locale));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        LOG.info("Country " + country.getName(Locale.getDefault()) + " added");

        addRegions(country.getRegions());

    }

    public void addRegion(Region region) {

        for (Locale locale : region.getNames().keySet()) {
            if (resourceDAO.getResourceIdByTranslation(region.getName(locale))>0) {
                throw new IllegalArgumentException("Region " + region.getName(locale) + " already exists");
            }
        }

        String sql = "INSERT INTO regions (name_resource_id, country) VALUE (?, ?)";
        int resId;

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            resId = resourceDAO.addResource();
            statement.setInt(1, resId);
            statement.setInt(2, region.getCountry().getId());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    region.setId(resultSet.getInt(1));
                }
            }

            for (Locale locale : region.getNames().keySet()) {
                resourceDAO.addTranslation(resId, locale, region.getName(locale));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        LOG.info("Region " + region.getName(Locale.getDefault()) + " added");

        addCities(region.getCities());
    }

    private void addRegions(List<Region> regionList) {
        for (Region region : regionList) {
            addRegion(region);
        }
    }


    public void addCity(City city) {

        for (Locale locale : city.getNames().keySet()) {
            City tryFind = findCityByName(city.getName(locale));
            if (tryFind != null && tryFind.getRegion().equals(city.getRegion())) {
                throw new IllegalArgumentException("City " + city.getName(locale) + " already exists");
            }
        }

        String sql = "INSERT INTO cities (region, name_resource_id, longitude, latitude) VALUE (?, ?, ?, ?)";
        int resId;

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            resId = resourceDAO.addResource();

            statement.setInt(1, city.getRegion().getId());
            statement.setInt(2, resId);
            statement.setDouble(3, city.getLongitude());
            statement.setDouble(4, city.getLatitude());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    city.setId(resultSet.getInt(1));
                    city.setNameResourceId(resId);
                }
            }

            for (Locale locale : city.getNames().keySet()) {
                resourceDAO.addTranslation(resId, locale, city.getNames().get(locale));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addCities(List<City> cityList) {
        for (City city : cityList) {
            addCity(city);
        }
    }

    public City findCityByName(String name) {

        int resourceId = resourceDAO.getResourceIdByTranslation(name);

        if (resourceId < 0) {
            return null;
        }

        String sql = "SELECT id, region, longitude, latitude FROM cities WHERE name_resource_id = ?";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {


            statement.setInt(1, resourceId);

            statement.executeQuery();

            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    Region region = getRegionById(resultSet.getInt(2));
                    double longitude = resultSet.getDouble(3);
                    double latitude = resultSet.getDouble(4);
                    Map<Locale, String> names = resourceDAO.getTranslations(resourceId);
                    City city = new City();
                    city.setId(id);
                    city.setRegion(region);
                    city.setLongitude(longitude);
                    city.setLatitude(latitude);
                    city.setNames(names);
                    city.setNameResourceId(resourceId);
                    return city;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    public City findCityById(int id) {
        String sql = "SELECT region, name_resource_id, longitude, latitude FROM cities WHERE id = ?";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeQuery();

            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {

                    Region region = getRegionById(resultSet.getInt(1));
                    int resourceId = resultSet.getInt(2);
                    double longitude = resultSet.getDouble(3);
                    double latitude = resultSet.getDouble(4);

                    Map<Locale, String> names = resourceDAO.getTranslations(resourceId);

                    City city = new City();
                    city.setRegion(region);
                    city.setLongitude(longitude);
                    city.setLatitude(latitude);
                    city.setNames(names);
                    city.setNameResourceId(resourceId);
                    return city;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;

    }

    public Region getRegionById(int id) {

        String sql = "SELECT name_resource_id, country FROM regions WHERE id = ?";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {


            statement.setInt(1, id);

            statement.executeQuery();

            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    int resourceId = resultSet.getInt(1);
                    Country country = getCountryById(resultSet.getInt(2));

                    Map<Locale, String> names = resourceDAO.getTranslations(resourceId);

                    Region region = new Region();
                    region.setNames(names);
                    region.setCountry(country);
                    region.setNameResourceId(resourceId);

                    return region;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    public Country getCountryById(int id) {
        String sql = "SELECT name_resource_id FROM countries WHERE id = ?";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeQuery();

            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    int resourceId = resultSet.getInt(1);
                    Map<Locale, String> names = resourceDAO.getTranslations(resourceId);

                    Country country = new Country();
                    country.setNameResourceId(resourceId);
                    country.setName(names);
                    return country;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void deleteCountry(Country country) {
        resourceDAO.deleteResource(country.getNameResourceId());
    }

    public void deleteRegion(Region region) {
        resourceDAO.deleteResource(region.getNameResourceId());
    }

    public void deleteCity(City city) {
        resourceDAO.deleteResource(city.getNameResourceId());
    }


//    public List<City> findAllCities() {
//        List<City> result = new LinkedList<>();
//        String sql = "SELECT DISTINCT id FROM cities";
//
//        try (Connection connection = DBManager.getInstance().getConnection();
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(sql)) {
//
//
//            while (resultSet.next()) {
//                int id = resultSet.getInt(1);
//                result.add(findCityById(id));
//            }
//
//        } catch (SQLException e) {
//            LOG.warn(e);
//        }
//        return result;
//    }

        public List<City> findAllCities() {
        List<City> result = new LinkedList<>();
        String sql = "select cities.id, region, name_resource_id, longitude, latitude, lang, country, translation " +
                "from cities \n" +
                "join resources on name_resource_id=resources.id\n" +
                "join translations on resource_id=resources.id\n" +
                "join locales on locales.id=locale_id\n" +
                "ORDER BY cities.id";



        try (Connection connection = DBManager.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            int lastId=0;
            City city = new City();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);

                String lang = resultSet.getString(6);
                String country =  resultSet.getString(7);
                String translation =  resultSet.getString(8);

                if(id != lastId){
                    if (lastId>0){
                        result.add(city);
                    }
                    lastId = id;
                    int regionId = resultSet.getInt(2);
                    int resourceId = resultSet.getInt(3);
                    double longitude = resultSet.getDouble(4);
                    double latitude = resultSet.getDouble(5);

                    Region tempRegion = new Region();
                    tempRegion.setId(regionId);
                    city = new City();

                    city.setRegion(tempRegion);
                    city.setLongitude(longitude);
                    city.setLatitude(latitude);
                    city.getNames().put(new Locale(lang, country), translation);
                    city.setNameResourceId(resourceId);
                }else {
                    city.getNames().put(new Locale(lang, country), translation);
                }
            }

        } catch (SQLException e) {
            LOG.warn(e);
        }
        return result;
    }


    public double findDistance(City cityA, City cityB) {
        return findDistance(cityA.getLongitude(), cityA.getLatitude(), cityB.getLongitude(), cityB.getLatitude());
    }


    private double findDistance(double lonA, double latA, double lonB, double latB) {
        double res = 0;
        String sql = "SELECT ST_Distance_Sphere(Point(?,?),Point(?,?)) as distance";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setDouble(1, lonA);
            statement.setDouble(2, latA);
            statement.setDouble(3, lonB);
            statement.setDouble(4, latB);

            statement.executeQuery();

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    res = resultSet.getDouble(1) / 1000;
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }
        return res;
    }
}
