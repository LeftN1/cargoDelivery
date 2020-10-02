package com.voroniuk.delivery.db.dao;

import com.voroniuk.delivery.db.entity.City;
import com.voroniuk.delivery.db.entity.Country;
import com.voroniuk.delivery.db.entity.Region;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

public class CityDAO {

    private static final Logger LOG = Logger.getLogger(CityDAO.class);

    private ResourceDAO resourceDAO;

    public CityDAO() {
        this.resourceDAO = resourceDAO = new ResourceDAO();
    }

    public void addCountry(Country country) {

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
                throw new IllegalArgumentException("City "+city.getName(locale) + " already exists");
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
                    city.setName(names);
                    return city;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public City getCityById(int id) {
        String sql = "SELECT region, name_resource_id, longitude, latitude FROM cities WHERE id = ?";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeQuery();

            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {

                    Region region = getRegionById(resultSet.getInt(1));
                    Map<Locale, String> names = resourceDAO.getTranslations(resultSet.getInt(2));
                    double longitude = resultSet.getDouble(3);
                    double latitude = resultSet.getDouble(4);

                    City city = new City();
                    city.setRegion(region);
                    city.setLongitude(longitude);
                    city.setLatitude(latitude);
                    city.setName(names);
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

                    Map<Locale, String> names = resourceDAO.getTranslations(resultSet.getInt(1));
                    Country country = getCountryById(resultSet.getInt(2));

                    Region region = new Region();
                    region.setName(names);
                    region.setCountry(country);

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

                    Map<Locale, String> names = resourceDAO.getTranslations(resultSet.getInt(1));
                    Country country = new Country();
                    country.setName(names);
                    return country;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
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
