package com.voroniuk.delivery.db.dao;

import com.voroniuk.delivery.db.entity.City;
import com.voroniuk.delivery.db.entity.Country;
import com.voroniuk.delivery.db.entity.Region;
import com.voroniuk.delivery.web.MainController;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class CityDAO {

    private static final Logger LOG = Logger.getLogger(CityDAO.class);

    public void addCountry(Country country) {
        String sql = "INSERT INTO countries (country_name) VALUES (?)";
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, country.getName());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    country.setId(resultSet.getInt(1));
                }
            }

            if (country.getRegions().size() > 0) {
                addRegions(country.getRegions());
            }

            LOG.info("Country has been added");
        } catch (SQLException e) {
            LOG.warn(e);
        }


    }


    public void addRegion(Region region) {
        List<Region> regions = new LinkedList<>();
        regions.add(region);
        addRegions(regions);
    }


    public void addRegions(List<Region> regions) {
        String sql = "INSERT INTO regions (region_name, country) VALUES (?,?)";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            for (Region region : regions) {
                statement.setString(1, region.getName());
                statement.setInt(2, region.getCountry().getId());
                statement.executeUpdate();
                connection.commit();
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        region.setId(resultSet.getInt(1));
                    }
                }
                if (region.getCities().size() > 0) {
                    addCities(region.getCities());
                }
            }


        } catch (SQLException e) {
            LOG.warn(e);
        }
    }


    public void addCity(City city) {
        List<City> cities = new LinkedList<>();
        cities.add(city);
        addCities(cities);
    }


    public void addCities(List<City> cities) {
        String sql = "INSERT INTO cities (region, city_name, longitude, latitude) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            for (City city : cities) {
                statement.setInt(1, city.getRegion().getId());
                statement.setString(2, city.getName());
                statement.setDouble(3, city.getLongitude());
                statement.setDouble(4, city.getLatitude());
                statement.executeUpdate();
                connection.commit();
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        city.setId(resultSet.getInt(1));
                        System.out.println(city.getId());
                    }
                }
            }

        } catch (SQLException e) {
            LOG.warn(e);
        }
    }


    public List<City> findAllCities() {
        List<City> res = new LinkedList<>();
        String sql = "select city_id, region_name, city_name, longitude, latitude\n" +
                "from cities\n" +
                "join regions\n" +
                "on region = region_id\n" +
                "order by city_id";
        try (Connection connection = DBManager.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                Region region = new Region(resultSet.getString(2));
                String cityName = resultSet.getString(3);
                double longitude = resultSet.getDouble(4);
                double latitude = resultSet.getDouble(5);
                res.add(new City(id, region, cityName, longitude, latitude));
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }
        return res;
    }


    public City getCity(String name) {
        String sql = "select city_id, region_name, city_name, longitude, latitude\n" +
                "from cities\n" +
                "join regions\n" +
                "on region = region_id\n" +
                "where city_name= ?";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);

            statement.executeQuery();

            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    Region region = new Region(resultSet.getString(2));
                    String cityName = resultSet.getString(3);
                    double longitude = resultSet.getDouble(4);
                    double latitude = resultSet.getDouble(5);
                    return new City(id, region, cityName, longitude, latitude);
                } else {
                    LOG.info("Can't find city " + name);
                }
            }
        } catch (SQLException e) {
            LOG.warn(e.getMessage());
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
