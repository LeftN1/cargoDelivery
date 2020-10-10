package com.voroniuk.delivery.db.dao;

import com.voroniuk.delivery.db.entity.*;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class OrderDAO {

    private static final Logger LOG = Logger.getLogger(OrderDAO.class);

    public void saveDelivery(Delivery delivery) {

        String sql = "insert into deliveries (user_id, origin_city_id, destination_city_id ,adress, cargo_type, weight, volume, cost) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, delivery.getUser().getId());
            statement.setInt(2, delivery.getOrigin().getId());
            statement.setInt(3, delivery.getDestination().getId());
            statement.setString(4, delivery.getAdress());
            statement.setInt(5, delivery.getType().getId());
            statement.setInt(6, delivery.getWeight());
            statement.setInt(7, delivery.getVolume());
            statement.setDouble(8, delivery.getCost());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    delivery.setId(resultSet.getInt(1));
                }
            }

            saveDeliveryStatuses(delivery);

            LOG.info("Delivery has been added");
        } catch (SQLException e) {
            LOG.warn(e);
        }
    }

    private void saveDeliveryStatuses(Delivery delivery) {
        String sql = "insert into delivery_status (delivery_id, status_id, date_time) " +
                "values (?, ?, ?)";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            for (Map.Entry<DeliveryStatus, Date> entry : delivery.getStatusMap().entrySet()) {
                statement.setInt(1, delivery.getId());
                statement.setInt(2, entry.getKey().getId());
                statement.setLong(3, entry.getValue().getTime());
                statement.executeUpdate();
            }
            LOG.info("Delivery statuses have been added");
        } catch (SQLException e) {
            LOG.warn(e);
        }
    }

    public List<Delivery> findUserDeliveries(User user, int start, int offset) {
        List<Delivery> deliveries = new LinkedList<>();

        CityDAO cityDAO = new CityDAO();


        String sql =    "select * from deliveries " +
                        "where user_id=? " +
                        "limit ?, ?";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setInt(1, user.getId());
            statement.setInt(2, start);
            statement.setInt(3, offset);

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt(1);

                    int userId = resultSet.getInt(2);
                    int originCityId = resultSet.getInt(3);
                    int destinationCityId = resultSet.getInt(4);
                    String adress = resultSet.getString(5);
                    CargoType type = CargoType.getTypeById(resultSet.getInt(6));
                    int weight = resultSet.getInt(7);
                    int volume = resultSet.getInt(8);
                    int cost = resultSet.getInt(9);

                    Delivery delivery = new Delivery();
                    delivery.setId(id);
                    delivery.setUser(user);
                    delivery.setOrigin(cityDAO.findCityById(originCityId));
                    delivery.setDestination(cityDAO.findCityById(destinationCityId));
                    delivery.setAdress(adress);
                    delivery.setType(type);
                    delivery.setWeight(weight);
                    delivery.setVolume(volume);
                    delivery.setCost(cost);
                    delivery.setStatusMap(findStatusesByDeliveryId(id));

                    deliveries.add(delivery);
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }

        return deliveries;
    }

    public List<Delivery> findUserDeliveries(User user) {
        int start = 0;
        int offset = countDeliveriesByUser(user);
        return findUserDeliveries(user, start, offset);
    }

    public List<Delivery> findDeliveriesByStatus(DeliveryStatus status, int start, int offset) {
        List<Delivery> deliveries = new LinkedList<>();

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();


        String sql = "select * from deliveries " +
                "join delivery_status on deliveries.id=delivery_id\n" +
                "where status_id=?\n" +
                "limit ? , ?";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setInt(1, status.getId());
            statement.setInt(2, start);
            statement.setInt(3, offset);

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt(1);

                    int userId = resultSet.getInt(2);
                    int originCityId = resultSet.getInt(3);
                    int destinationCityId = resultSet.getInt(4);
                    String adress = resultSet.getString(5);
                    CargoType type = CargoType.getTypeById(resultSet.getInt(6));
                    int weight = resultSet.getInt(7);
                    int volume = resultSet.getInt(8);
                    int cost = resultSet.getInt(9);

                    Delivery delivery = new Delivery();
                    delivery.setId(id);
                    delivery.setUser(userDAO.findUserById(userId));
                    delivery.setOrigin(cityDAO.findCityById(originCityId));
                    delivery.setDestination(cityDAO.findCityById(destinationCityId));
                    delivery.setAdress(adress);
                    delivery.setType(type);
                    delivery.setWeight(weight);
                    delivery.setVolume(volume);
                    delivery.setCost(cost);
                    delivery.setStatusMap(findStatusesByDeliveryId(id));

                    deliveries.add(delivery);
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }

        return deliveries;
    }

    public List<Delivery> findDeliveriesByStatus(DeliveryStatus status) {
        int start = 0;
        int offset = countDeliveriesByStatus(status);
        return findDeliveriesByStatus(status, start, offset);
    }

    public int countDeliveriesByStatus() {
        int result = 0;
        String sql = "select count(*) from deliveries;";


        try (Connection connection = DBManager.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {


            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            LOG.warn(e);
        }
        return result;
    }

    public int countDeliveriesByStatus(DeliveryStatus status) {
        int result = 0;

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();


        String sql = "select count(*) from deliveries " +
                "join delivery_status on deliveries.id=delivery_id " +
                "where status_id=?";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setInt(1, status.getId());
            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }

        return result;
    }

    public int countDeliveriesByUser(User user) {
        int result = 0;

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();


        String sql = "select count(*) from deliveries " +
                "join delivery_status on deliveries.id=delivery_id " +
                "where user_id=?";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setInt(1, user.getId());
            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }

        return result;
    }


//    public List<Delivery> findAllDeliveries() {
//        List<Delivery> deliveries = new LinkedList<>();
//
//        CityDAO cityDAO = new CityDAO();
//        UserDAO userDAO = new UserDAO();
//
//
//        String sql =    "select * from deliveries " +
//                        "join delivery_status on deliveries.id=delivery_id ";
//
//        try (Connection connection = DBManager.getInstance().getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql);) {
//
//            statement.setInt(1, status.getId());
//            statement.executeQuery();
//            try (ResultSet resultSet = statement.getResultSet()) {
//
//                while (resultSet.next()) {
//                    int id = resultSet.getInt(1);
//
//                    int userId = resultSet.getInt(2);
//                    int originCityId = resultSet.getInt(3);
//                    int destinationCityId = resultSet.getInt(4);
//                    String adress = resultSet.getString(5);
//                    CargoType type = CargoType.getTypeById(resultSet.getInt(6));
//                    int weight = resultSet.getInt(7);
//                    int volume = resultSet.getInt(8);
//                    int cost = resultSet.getInt(9);
//
//                    Delivery delivery = new Delivery();
//                    delivery.setId(id);
//                    delivery.setUser(userDAO.findUserById(userId));
//                    delivery.setOrigin(cityDAO.findCityById(originCityId));
//                    delivery.setDestination(cityDAO.findCityById(destinationCityId));
//                    delivery.setAdress(adress);
//                    delivery.setType(type);
//                    delivery.setWeight(weight);
//                    delivery.setVolume(volume);
//                    delivery.setCost(cost);
//                    delivery.setStatusMap(findStatusesByDeliveryId(id));
//
//                    deliveries.add(delivery);
//                }
//            }
//        } catch (SQLException e) {
//            LOG.warn(e);
//        }
//
//        return deliveries;
//    }

    public Map<DeliveryStatus, Date> findStatusesByDeliveryId(int id) {

        Map<DeliveryStatus, Date> statuses = new HashMap<>();

        String sql = "select status_id, date_time from delivery_status " +
                "where delivery_id=?";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeQuery();

            try (ResultSet resultSet = statement.getResultSet()) {

                while (resultSet.next()) {
                    int statusId = resultSet.getInt(1);
                    long millis = resultSet.getLong(2);

                    statuses.put(DeliveryStatus.getStatusById(statusId), new Date(millis));
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }
        return statuses;
    }

}
