package com.voroniuk.delivery.db.dao;


import com.voroniuk.delivery.db.entity.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class DBManager {
    Logger logger = Logger.getLogger(DBManager.class.getName());

    private static DBManager dbManager;
    private String conUrl;

    private DBManager() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("app.properties"));
            conUrl = (properties.getProperty("connection.url"));

        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(conUrl);
    }


    public void addUser(User user) {
        String sql = "INSERT INTO users (login) VALUES (?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getLogin());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getInt(1));
                }
            }

            logger.info("User has been added");
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }




    public List<User> findAllUsers() {
        List<User> res = new LinkedList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String login = resultSet.getString(2);
                res.add(new User(id, login));
            }

        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return res;
    }




}