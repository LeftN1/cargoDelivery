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


}