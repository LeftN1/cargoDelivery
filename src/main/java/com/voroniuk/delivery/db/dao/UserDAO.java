package com.voroniuk.delivery.db.dao;

import com.voroniuk.delivery.db.entity.Role;
import com.voroniuk.delivery.db.entity.User;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDAO {
    private static final Logger LOG = Logger.getLogger(UserDAO.class);

    public void saveUser(User user) {
        String sql = "INSERT INTO users (login, role, pass) VALUES (?,?,?)";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getLogin());
            statement.setInt(2, getRoleNum(user.getRole()));
            statement.setString(3, user.getPassword());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getInt(1));
                }
            }

            LOG.info("User has been added");
        } catch (SQLException e) {
            LOG.warn(e);
        }
    }

//    public Role getRoleFromDb(String name) {
//        String sql = "SELECT * FROM roles WHERE role_name=?";
//        try (Connection connection = DBManager.getInstance().getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql);) {
//
//            statement.setString(1, name);
//            statement.executeQuery();
//            try (ResultSet resultSet = statement.getResultSet()) {
//
//                if (resultSet.next()) {
//                    for (Role role : Role.values()) {
//                        if (role.name().equals(resultSet.getString(2))) {
//                            return role;
//                        }
//                    }
//                } else {
//                    LOG.info("Can't find user " + name);
//                }
//            }
//        } catch (SQLException e) {
//            LOG.warn(e);
//        }
//        return null;
//    }

    public int getRoleNum(Role role) {
        String sql = "SELECT * FROM roles WHERE role_name=?";
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setString(1, role.name());
            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    for (Role r : Role.values()) {
                        if (r.name().equals(resultSet.getString(2))) {
                            return resultSet.getInt(1);
                        }
                    }
                } else {
                    LOG.info("Can't find role " + role.name());
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }
        return -1;
    }

    public User findUserByLogin(String login) {
        String sql =    "select users.id, pass, role_name from users " +
                        "join roles on role=roles.id " +
                        "where login=?";
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setString(1, login);
            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String password = resultSet.getString(2);
                    Role role = Role.valueOf(resultSet.getString(3));

                    User user = new User();
                    user.setId(id);
                    user.setLogin(login);
                    user.setPassword(password);
                    user.setRole(role);
                    return user;
                } else {
                    LOG.info("Can't find user " + login);
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }
        return null;
    }

    public User findUserById(int id) {
        String sql =    "select login, pass, role_name from users " +
                        "join roles on role=roles.id " +
                        "where users.id=?";
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setInt(1, id);
            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    String login = resultSet.getString(1);
                    String password = resultSet.getString(2);
                    Role role = Role.valueOf(resultSet.getString(3));

                    User user = new User();
                    user.setId(id);
                    user.setLogin(login);
                    user.setPassword(password);
                    user.setRole(role);
                    LOG.debug("User founded: " + user);
                    return user;
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
            LOG.info("Can't find userby id: " + id);
        }
        return null;
    }


    public List<User> findAllUsers() {
        List<User> res = new LinkedList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = DBManager.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String login = resultSet.getString(2);
                res.add(new User(id, login));
            }

        } catch (SQLException e) {
            LOG.warn(e);
        }
        return res;
    }

    public void deleteUser(User user) {
        String sql = "DELETE FROM users WHERE login=?";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getLogin());
            statement.executeUpdate();

            LOG.info("User has been deleted");
        } catch (SQLException e) {
            LOG.warn(e);
        }
    }

}
