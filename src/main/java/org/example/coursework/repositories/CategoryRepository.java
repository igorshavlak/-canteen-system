package org.example.coursework.repositories;

import org.example.coursework.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    private static final String GET_CATEGORY_ID_BY_NAME = "SELECT category_id FROM category WHERE name = ?";

    private static final String GET_ALL_CATEGORIES_NAMES = "SELECT name FROM category";


    public int getCategoryIdByName(String categoryName) {
        int categoryId = -1;
        String name = "";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CATEGORY_ID_BY_NAME)) {
            preparedStatement.setString(1, categoryName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    categoryId = resultSet.getInt("category_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryId;
    }
    public List<String> getAllCategoriesNames() {
        List<String> categoryNames = new ArrayList<>();
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_CATEGORIES_NAMES);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                categoryNames.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryNames;
    }

}
