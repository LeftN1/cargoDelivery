package com.voroniuk.delivery.db.dao;

import com.voroniuk.delivery.db.entity.*;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 *
 * DAO for create-update-delete Deliveries
 *
 * @author M. Voroniuk
 */

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

    /**
     * Save delivery statuses with date in database.
     * @param delivery Delivery
     */
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

    /**
     *
     * Add new status with date-time to the Delivery
     *
     * @param delivery Delivery
     * @param status Status
     * @param date UNIX time (in millis from 00:00:00 UTC on 1 January 1970)
     *
     */

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
        delivery.getStatusMap().put(status, date);
    }

    public Delivery findDeliveryById(int id) {

        Delivery delivery = null;

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();


        String sql = "select * from deliveries\n" +
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
                    delivery.setAddress(adress);
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

    /**
     * Find  deliveries that have last staatus {@status}, by status and user and/or origin/destination city and/or date
     * @param status  Delivery status. Search by last status.
     * @param userId users id. If zero - finds all users.
     * @param originId Origin city id. If zero - finds all city.
     * @param destinationId Destination city id. If zero - finds all.
     * @param startDate Start date of search. If zero(in millis) - finds for all time.
     * @param endDate End date of searc. If zero(in millis) - finds for all time.
     * @param start Skip first n deliveries.
     * @param offset Max count of deliveries.
     * @return list of deliveries.
     */

    public List<Delivery> findDeliveriesByStatusAndUserIdAndDate(DeliveryStatus status, int userId, int originId, int destinationId, Date startDate, Date endDate, int start, int offset) {
        List<Delivery> deliveries = new LinkedList<>();

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();
        User user = userDAO.findUserById(userId);

        if (endDate.getTime() < startDate.getTime()) {
            endDate = new Date(0);
        }

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
                "and CASE\n" +
                "WHEN ? > 0 THEN actualdelivery_status.date_time > ?\n" +
                "ELSE true\n" +
                "END\n" +
                "and CASE\n" +
                "WHEN ? > 0 THEN actualdelivery_status.date_time < ?\n" +
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
            statement.setLong(8, startDate.getTime());
            statement.setLong(9, startDate.getTime());
            statement.setLong(10, endDate.getTime());
            statement.setLong(11, endDate.getTime());
            statement.setInt(12, start);
            statement.setInt(13, offset);

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt(1);

//                    int uId = resultSet.getInt(2);
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
                    delivery.setAddress(adress);
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

    /**
     *
     * Find deliveries that have {@status} in their history by status and user and/or origin/destination city and/or date.
     * This method is used for making reports to see deliveries that had definite status in definite period.
     * @param status  Delivery status . Search by all statuses.
     * @param userId users id. If zero - finds all users.
     * @param originId Origin city id. If zero - finds all city.
     * @param destinationId Destination city id. If zero - finds all.
     * @param startDate Start date of search. If zero(in millis) - finds for all time.
     * @param endDate End date of searc. If zero(in millis) - finds for all time.
     * @param start Skip first n deliveries.
     * @param offset Max count of deliveries.
     * @return list of deliveries.
     */
    public List<Delivery> reportDeliveriesByStatusAndUserIdAndDate(DeliveryStatus status, int userId, int originId, int destinationId, Date startDate, Date endDate, int start, int offset) {
        List<Delivery> deliveries = new LinkedList<>();

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();

        if (endDate.getTime() < startDate.getTime()) {
            endDate = new Date(0);
        }

        String sql = "SELECT id, user_id, origin_city_id, destination_city_id, adress, cargo_type, weight, volume, cost, delivery_id, status_id FROM deliveries \n" +
                "join delivery_status on id=delivery_id\n" +
                "where status_id=?\n" +
                "and CASE\n" +
                "WHEN ? > 0 THEN origin_city_id = ?\n" +
                "ELSE true\n" +
                "END\n" +
                "and CASE\n" +
                "WHEN ? > 0 THEN destination_city_id = ?\n" +
                "ELSE true\n" +
                "END\n" +
                "and CASE\n" +
                "WHEN ? > 0 THEN user_id = ?\n" +
                "ELSE true\n" +
                "END\n" +
                "and CASE\n" +
                "WHEN ? > 0 THEN date_time > ?\n" +
                "ELSE true\n" +
                "END\n" +
                "and CASE\n" +
                "WHEN ? > 0 THEN date_time < ?\n" +
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
            statement.setLong(8, startDate.getTime());
            statement.setLong(9, startDate.getTime());
            statement.setLong(10, endDate.getTime());
            statement.setLong(11, endDate.getTime());
            statement.setInt(12, start);
            statement.setInt(13, offset);

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
                    delivery.setAddress(adress);
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

    public List<Delivery> findDeliveriesByStatusAndCityId(DeliveryStatus status, int originId, int destinationId, int start, int offset) {
        return findDeliveriesByStatusAndUserIdAndDate(status, 0, originId, destinationId, new Date(0), new Date(0), start, offset);
    }

    public List<Delivery> findDeliveriesByStatusAndCityIdAndDate(DeliveryStatus status, int originId, int destinationId, Date startDate, Date endDate, int start, int offset) {
        return findDeliveriesByStatusAndUserIdAndDate(status, 0, originId, destinationId, startDate, endDate, start, offset);
    }

    public List<Delivery> reportDeliveriesByStatusAndCityIdAndDate(DeliveryStatus status, int originId, int destinationId, Date startDate, Date endDate, int start, int offset) {
        return reportDeliveriesByStatusAndUserIdAndDate(status, 0, originId, destinationId, startDate, endDate, start, offset);
    }

    public List<Delivery> findDeliveriesByStatus(DeliveryStatus status, int start, int offset) {
        return findDeliveriesByStatusAndUserIdAndDate(status, 0, 0, 0, new Date(0), new Date(0), start, offset);
    }

    public List<Delivery> findUserDeliveries(DeliveryStatus status, User user, int start, int offset) {
        return findDeliveriesByStatusAndUserIdAndDate(status, user.getId(), 0, 0, new Date(0), new Date(0), start, offset);
    }

    public List<Delivery> findDeliveriesByStatus(DeliveryStatus status) {
        int start = 0;
        int offset = countDeliveriesByStatus(status);
        return findDeliveriesByStatus(status, start, offset);
    }

    /**
     * Counts number of Deliveries by status and user and/or origin/destination city and/or date.
     * Counts only those deliveries that have {@status} for their last status
     * @param status  Delivery status . Search by all statuses.
     * @param userId users id. If zero - finds all users.
     * @param originId Origin city id. If zero - finds all city.
     * @param destinationId Destination city id. If zero - finds all.
     * @param startDate Start date of search. If zero(in millis) - finds for all time.
     * @param endDate End date of searc. If zero(in millis) - finds for all time.
     * @return count of deliveries
     *
     */

    public int countDeliveries(DeliveryStatus status, int userId, int originId, int destinationId, Date startDate, Date endDate) {
        int result = 0;

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();


        String sql = "select  count(*)" +
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
                "and CASE\n" +
                "WHEN ? > 0 THEN actualdelivery_status.date_time > ?\n" +
                "ELSE true\n" +
                "END\n" +
                "and CASE\n" +
                "WHEN ? > 0 THEN actualdelivery_status.date_time < ?\n" +
                "ELSE true\n" +
                "END";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setInt(1, status.getId());
            statement.setInt(2, originId);
            statement.setInt(3, originId);
            statement.setInt(4, destinationId);
            statement.setInt(5, destinationId);
            statement.setInt(6, userId);
            statement.setInt(7, userId);
            statement.setLong(8, startDate.getTime());
            statement.setLong(9, startDate.getTime());
            statement.setLong(10, endDate.getTime());
            statement.setLong(11, endDate.getTime());

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

    /**
     * Counts number of Deliveries by status and user and/or origin/destination city and/or date.
     * Counts deliveries that have {@status} in their history
     * @param status  Delivery status . Search by all statuses.
     * @param userId users id. If zero - finds all users.
     * @param originId Origin city id. If zero - finds all city.
     * @param destinationId Destination city id. If zero - finds all.
     * @param startDate Start date of search. If zero(in millis) - finds for all time.
     * @param endDate End date of searc. If zero(in millis) - finds for all time.
     * @return count of deliveries
     *
     */
    public int countReportDeliveries(DeliveryStatus status, int userId, int originId, int destinationId, Date startDate, Date endDate) {
        int result = 0;

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();


        String sql = "SELECT count(*) FROM deliveries \n" +
                "join delivery_status on id=delivery_id\n" +
                "where status_id=?\n" +
                "and CASE\n" +
                "WHEN ? > 0 THEN origin_city_id = ?\n" +
                "ELSE true\n" +
                "END\n" +
                "and CASE\n" +
                "WHEN ? > 0 THEN destination_city_id = ?\n" +
                "ELSE true\n" +
                "END\n" +
                "and CASE\n" +
                "WHEN ? > 0 THEN user_id = ?\n" +
                "ELSE true\n" +
                "END\n" +
                "and CASE\n" +
                "WHEN ? > 0 THEN date_time > ?\n" +
                "ELSE true\n" +
                "END\n" +
                "and CASE\n" +
                "WHEN ? > 0 THEN date_time < ?\n" +
                "ELSE true\n" +
                "END\n";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setInt(1, status.getId());
            statement.setInt(2, originId);
            statement.setInt(3, originId);
            statement.setInt(4, destinationId);
            statement.setInt(5, destinationId);
            statement.setInt(6, userId);
            statement.setInt(7, userId);
            statement.setLong(8, startDate.getTime());
            statement.setLong(9, startDate.getTime());
            statement.setLong(10, endDate.getTime());
            statement.setLong(11, endDate.getTime());

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
        int userId = user.getId();

        String sql = "SELECT count(*) FROM deliveries \n" +
                        "where user_id=?\n";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setInt(1, userId);


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


    public int countDeliveriesByStatusAndUser(DeliveryStatus status, User user) {
        return countDeliveries(status, user.getId(), 0, 0, new Date(0), new Date(0));
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

        String sql = "delete from deliveries where id=?";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, id);

            statement.executeUpdate();

            LOG.info("Delivery has been deleted");
        } catch (SQLException e) {
            LOG.warn(e);
        }
    }

    public void updateDelivery(Delivery delivery) {

        String sql = "update deliveries\n" +
                "set origin_city_id=?, destination_city_id=?,\n" +
                "adress=?, cargo_type=?, weight=?, volume=?, cost=?\n" +
                "where id=?;";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, delivery.getOrigin().getId());
            statement.setInt(2, delivery.getDestination().getId());
            statement.setString(3, delivery.getAdress());
            statement.setInt(4, delivery.getType().getId());
            statement.setInt(5, delivery.getWeight());
            statement.setInt(6, delivery.getVolume());
            statement.setDouble(7, delivery.getCost());
            statement.setInt(8, delivery.getId());

            statement.executeUpdate();

            LOG.info("Delivery has been updated");
        } catch (SQLException e) {
            LOG.warn(e);
        }
    }

}
