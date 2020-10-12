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

    public void changeCurrentStatus(Delivery delivery, DeliveryStatus status, Date date) {
        String sql = "insert into delivery_status (delivery_id, status_id, date_time) " +
                "values (?, ?, ?)";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {


            statement.setInt(1, delivery.getId());
            statement.setInt(2, status.getId());
            statement.setLong(3, date.getTime());
            statement.executeUpdate();

            LOG.info("Statuses have been added to th delivery id=" + delivery.getId());
        } catch (SQLException e) {
            LOG.warn(e);
        }
    }

    public Delivery findDeliveryById(int id){

        Delivery delivery = null;

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();


        String sql =    "select * from deliveries\n" +
                        "where id=?\n" +
                        "limit 1";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setInt(1, id);


            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {

                    int uId = resultSet.getInt(2);
                    int originCityId = resultSet.getInt(3);
                    int destinationCityId = resultSet.getInt(4);
                    String adress = resultSet.getString(5);
                    CargoType type = CargoType.getTypeById(resultSet.getInt(6));
                    int weight = resultSet.getInt(7);
                    int volume = resultSet.getInt(8);
                    int cost = resultSet.getInt(9);

                    delivery = new Delivery();
                    delivery.setId(id);
                    delivery.setUser(userDAO.findUserById(uId));
                    delivery.setOrigin(cityDAO.findCityById(originCityId));
                    delivery.setDestination(cityDAO.findCityById(destinationCityId));
                    delivery.setAdress(adress);
                    delivery.setType(type);
                    delivery.setWeight(weight);
                    delivery.setVolume(volume);
                    delivery.setCost(cost);
                    delivery.setStatusMap(findStatusesByDeliveryId(id));

                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }

        return delivery;
    }



    public List<Delivery> findDeliveriesByStatusAndUserId(DeliveryStatus status, int userId, int originId, int destinationId, int start, int offset) {
        List<Delivery> deliveries = new LinkedList<>();

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();


        String sql = "select  actualdelivery_status.delivery_id, deliveries.user_id, \n" +
                "deliveries.origin_city_id, deliveries.destination_city_id, deliveries.adress, \n" +
                "    deliveries.cargo_type, deliveries.weight, \n" +
                "    deliveries.volume, deliveries.cost,\n" +
                "actualdelivery_status.date_time , delivery_status.status_id\n" +
                "from \n" +
                "(select delivery_id, max(date_time) as date_time\n" +
                "    from delivery_status\n" +
                "group by delivery_id) as actualdelivery_status\n" +
                "inner join delivery_status \n" +
                "on actualdelivery_status.delivery_id = delivery_status.delivery_id\n" +
                "and actualdelivery_status.date_time = delivery_status.date_time\n" +
                "    and delivery_status.status_id=?\n" +
                "inner join deliveries on deliveries.id = actualdelivery_status.delivery_id\n" +
                "and CASE\n" +
                "WHEN ? > 0 THEN deliveries.origin_city_id = ?\n" +
                "ELSE true\n" +
                "END\n" +
                "and CASE\n" +
                "WHEN ? > 0 THEN deliveries.destination_city_id = ?\n" +
                "ELSE true\n" +
                "END\n" +
                "and CASE\n" +
                "WHEN ? > 0 THEN deliveries.user_id = ?\n" +
                "ELSE true\n" +
                "END\n" +
                "limit ?, ?;";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setInt(1, status.getId());
            statement.setInt(2, originId);
            statement.setInt(3, originId);
            statement.setInt(4, destinationId);
            statement.setInt(5, destinationId);
            statement.setInt(6, userId);
            statement.setInt(7, userId);
            statement.setInt(8, start);
            statement.setInt(9, offset);

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt(1);

                    int uId = resultSet.getInt(2);
                    int originCityId = resultSet.getInt(3);
                    int destinationCityId = resultSet.getInt(4);
                    String adress = resultSet.getString(5);
                    CargoType type = CargoType.getTypeById(resultSet.getInt(6));
                    int weight = resultSet.getInt(7);
                    int volume = resultSet.getInt(8);
                    int cost = resultSet.getInt(9);

                    Delivery delivery = new Delivery();
                    delivery.setId(id);
                    delivery.setUser(userDAO.findUserById(uId));
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

    public List<Delivery> findDeliveriesByStatusAndCityId(DeliveryStatus status, int originId, int destinationId,  int start, int offset) {
        return findDeliveriesByStatusAndUserId(status, 0, originId, destinationId, start, offset);
    }

    public List<Delivery> findDeliveriesByStatus(DeliveryStatus status, int start, int offset) {
        return findDeliveriesByStatusAndUserId(status, 0, 0, 0, start, offset);
    }

    public List<Delivery> findUserDeliveries(DeliveryStatus status, User user, int start, int offset) {
        return findDeliveriesByStatusAndUserId(status, user.getId(), 0, 0, start, offset);
    }

    public List<Delivery> findDeliveriesByStatus(DeliveryStatus status) {
        int start = 0;
        int offset = countDeliveriesByStatus(status);
        return findDeliveriesByStatus(status, start, offset);
    }

    public int countDeliveriesByStatusAndUser(DeliveryStatus status, User user) {
        int result = 0;

        String sql =    "select count(*)\n" +
                        "from \n" +
                        "(select delivery_id, max(date_time) as date_time\n" +
                        "    from delivery_status\n" +
                        "group by delivery_id) as actualdelivery_status\n" +
                        "inner join delivery_status \n" +
                        "on actualdelivery_status.delivery_id = delivery_status.delivery_id\n" +
                        "and actualdelivery_status.date_time = delivery_status.date_time\n" +
                        "    and delivery_status.status_id=?\n" +
                        "inner join deliveries on deliveries.id = actualdelivery_status.delivery_id\n" +
                        "and CASE\n" +
                        "WHEN ? > 0 THEN deliveries.user_id = ?\n" +
                        "ELSE true\n" +
                        "END";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setInt(1, status.getId());
            statement.setInt(2, user.getId());
            statement.setInt(3, user.getId());

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

    public int countDeliveriesByStatus(DeliveryStatus status) {
        User user = new User();
        user.setId(0);
        return countDeliveriesByStatusAndUser(status, user);
    }

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

    public void deleteDeliveryById(int id) {

        String sql =    "delete from deliveries where id=?";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, id);

            statement.executeUpdate();

            LOG.info("Delivery has been deleted");
        } catch (SQLException e) {
            LOG.warn(e);
        }
    }

}
