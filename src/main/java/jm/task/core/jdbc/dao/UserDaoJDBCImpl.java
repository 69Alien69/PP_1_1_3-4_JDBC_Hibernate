package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() {
        if (!checkTable()) {
            String query = "CREATE TABLE `users` (" +
                    "  `id` BIGINT NOT NULL AUTO_INCREMENT," +
                    "  `Name` VARCHAR(45) NOT NULL," +
                    "  `Lastname` VARCHAR(45) NOT NULL," +
                    "  `Age` TINYINT NOT NULL," +
                    "  PRIMARY KEY (`id`));";
            execute(query);
        }
    }

    @Override
    public void dropUsersTable() {
        if (checkTable()) {
            String query = "DROP TABLE users;";
            execute(query);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String SQL = "INSERT INTO users (Name, Lastname, Age) VALUES (?, ?, ?);";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        String SQL = "DELETE FROM users WHERE id = ?";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<User> getAllUsers() {
        String SQL = "SELECT * FROM users;";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<User> userList = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastname = resultSet.getString("lastname");
                byte age = resultSet.getByte("age");
                userList.add(new User(name, lastname, age));
                userList.get(userList.size()-1).setId(id);
            }
            return userList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUsersTable() {
        String SQL = "TRUNCATE users";
        execute(SQL);
    }

    private void execute(String SQL) {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkTable() {
        try (Connection connection = Util.getConnection();
             ResultSet resultSet= connection.getMetaData().getTables(null, null,
                "users", new String[] {"TABLE"})) {
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
