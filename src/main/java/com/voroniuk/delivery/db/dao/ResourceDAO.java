package com.voroniuk.delivery.db.dao;

import com.voroniuk.delivery.db.entity.Region;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ResourceDAO {

    private static final Logger LOG = Logger.getLogger(ResourceDAO.class);

    public int addResource() throws SQLException {
        String sqlAddResource = "INSERT INTO resources VALUE(DEFAULT)";
        int resourceId;

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement psAddRes = connection.prepareStatement(sqlAddResource, Statement.RETURN_GENERATED_KEYS)) {

            psAddRes.executeUpdate();

            try (ResultSet resultSet = psAddRes.getGeneratedKeys()) {
                if (resultSet.next()) {
                    resourceId = resultSet.getInt(1);
                } else {
                    throw new SQLException("Can't add resource");
                }
            }
            return resourceId;
        }
    }

    public void addTranslation(int resourceId, Locale locale, String translation) throws SQLException {
        String sqlAddTranslation = "INSERT INTO translations VALUES(?, ?, ?)";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement psAddTranslation = connection.prepareStatement(sqlAddTranslation)) {

            psAddTranslation.setInt(1, resourceId);
            psAddTranslation.setInt(2, getLocaleId(locale));
            psAddTranslation.setString(3, translation);
            psAddTranslation.executeUpdate();
        }
    }

    public int getResourceIdByTranslation(String translation) {

        String sql = "SELECT resource_id FROM translations WHERE translation=?";
        int resourceId;

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, translation);

            statement.executeQuery();

            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    resourceId = resultSet.getInt(1);
                    return resourceId;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }

    public Map<Locale, String> getTranslations(int resourceId) {

        Map<Locale, String> result = new HashMap<>();
        String sql = "SELECT locale_id, translation FROM translations WHERE resource_id=?";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, resourceId);
            statement.executeQuery();

            try (ResultSet resultSet = statement.getResultSet()) {
                while (resultSet.next()) {
                    Locale locale = getLocaleById(resultSet.getInt(1));
                    String translation = resultSet.getString(2);
                    result.put(locale, translation);
                }
            }

        } catch (SQLException e) {
            LOG.warn(e);
        }
        return result;
    }

    public Locale getLocaleById(int id) throws SQLException {
        String sql = "SELECT lang, country FROM locales WHERE id=?";

        Locale locale;

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeQuery();

            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    String language = resultSet.getString(1);
                    String country = resultSet.getString(2);
                    locale = new Locale(language, country);
                    return locale;
                } else {
                    throw new SQLException("Can't find locale with Id = " + id);
                }
            }
        }
    }


    public int getLocaleId(Locale locale) throws SQLException {

        String sql = "SELECT id FROM locales WHERE locales.lang=?";
        int localeId;

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, locale.getLanguage());

            statement.executeQuery();

            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    localeId = resultSet.getInt(1);
                    return localeId;
                } else {
                    throw new SQLException("Can't find locale with language = " + locale.getLanguage());
                }
            }
        }
    }

    public void deleteResource(int id){
        String sql = "DELETE FROM resources WHERE id=?";


        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
