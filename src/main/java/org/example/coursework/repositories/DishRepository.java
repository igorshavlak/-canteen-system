package org.example.coursework.repositories;

import org.example.coursework.DbConnection;
import org.example.coursework.entities.Dish;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DishRepository {

    public void createDish(Dish dish) {
        String query = "INSERT INTO dishes (name,part_image,description,calories,category_id,price) VALUES (?,?,?,?,?,?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, dish.getName());
            preparedStatement.setBytes(2, dish.getImage());
            preparedStatement.setString(3, dish.getDescription());
            preparedStatement.setInt(4, dish.getCalories());
            preparedStatement.setInt(5, dish.getCategoryId());
            preparedStatement.setDouble(6, dish.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDish(Dish dish) {
        String query = "UPDATE dishes SET name = ?, part_image = ?, description = ?, calories = ?, category_id = ?,price =? WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, dish.getName());
            preparedStatement.setDouble(2, dish.getPrice());
            preparedStatement.setInt(3, dish.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteDish(int dishId) {
        String query = "DELETE FROM dishes WHERE dish_id = ?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, dishId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Dish> getAllDishes() {
        List<Dish> dishes = new ArrayList<>();
        String query = "SELECT * FROM dishes";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                dishes.add(new Dish(
                        resultSet.getInt("dish_id"),
                        resultSet.getString("name"),
                        resultSet.getBytes("part_image"),
                        resultSet.getString("description"),
                        resultSet.getInt("calories"),
                        resultSet.getInt("price"),
                        resultSet.getInt("category_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishes;
    }
    public List<String> getDishNames() {
        String query = "SELECT name FROM dishes";
        List<String> dishNames = new ArrayList<>();
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                dishNames.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishNames;
    }
    // Метод для получения списка блюд по orderId
    public List<Dish> getOrderDishes(int orderId) throws SQLException {
        List<Dish> dishes = new ArrayList<>();

        String query = "SELECT d.dish_id, d.name, d.description, d.calories, d.price " +
                "FROM order_dishes od " +
                "JOIN dishes d ON od.dish_id = d.dish_id " +
                "WHERE od.order_id = ?";

        try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, orderId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int dishId = resultSet.getInt("dish_id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    int calories = resultSet.getInt("calories");
                    double price = resultSet.getDouble("price");

                    // Создаем объект Dish без изображения
                    Dish dish = new Dish(dishId, name, null, description, calories, price, 0);

                    dishes.add(dish);
                }
            }
        }

        return dishes;
    }

}
