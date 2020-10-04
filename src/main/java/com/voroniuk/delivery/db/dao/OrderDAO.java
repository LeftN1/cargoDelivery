package com.voroniuk.delivery.db.dao;

import com.voroniuk.delivery.db.entity.Delivery;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDAO {

    private static final Logger LOG = Logger.getLogger(OrderDAO.class);

    public void saveDelivery(Delivery delivery){

        String sql =    "insert into deliveries (user_id, city_id, adress, cargo_type, weight, volume, cost) " +
                        "values (?, ?, ?, ?, ?, ?, ?);\n";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, delivery.getUser().getId());
            statement.setInt(2, delivery.getCity().getId());
            statement.setString(3, delivery.getAdress());
            statement.setInt(4, delivery.getType().getId());
            statement.setInt(5, delivery.getWeight());
            statement.setInt(6, delivery.getVolume());
            statement.setDouble(7, delivery.getCost());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    delivery.setId(resultSet.getInt(1));
                }
            }

            LOG.info("User has been added");
        } catch (SQLException e) {
            LOG.warn(e);
        }

    }

}
