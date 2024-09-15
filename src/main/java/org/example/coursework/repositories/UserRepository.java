package org.example.coursework.repositories;

import org.example.coursework.DbConnection;
import org.example.coursework.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public static User login(String login, String password) {
        User user = null;
        String query = "SELECT u.login, u.password, r.name as role FROM users u JOIN roles r ON u.role_id = r.role_id WHERE u.login = ? AND u.password = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                user = new User(login, password, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
