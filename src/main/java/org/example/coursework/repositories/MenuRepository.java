package org.example.coursework.repositories;

import org.example.coursework.DbConnection;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.TextArea;
import org.example.coursework.entities.Dish;

public class MenuRepository {

    private Connection connection;

    public MenuRepository() {
        try {
            connection = DbConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle connection error
        }
    }

    public Map<String, List<String>> getAllMenus() {

        Map<String, List<String>> menus = new HashMap<>();
        menus.put("Breakfast", getDishesInMenu(1)); // Assuming menu_id 1 is for Breakfast
        menus.put("Lunch", getDishesInMenu(2));     // Assuming menu_id 2 is for Lunch
        menus.put("Dinner", getDishesInMenu(3));    // Assuming menu_id 3 is for Dinner
        return menus;
    }
    public boolean addDishToMenu(int menuId, int dishId) {
        if(isDishInMenu(menuId,dishId)){
            return false;
        }
        String sql = "INSERT INTO menu_dishes (menu_id, dish_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, menuId);
            statement.setInt(2, dishId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Dish> getDishesByMenuId(int menuId) {
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT d.id, d.name, d.image, d.description, d.calories, d.price, d.category_id " +
                "FROM menu_dishes md " +
                "JOIN dish d ON md.dish_id = d.id " +
                "WHERE md.menu_id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, menuId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                byte[] image = resultSet.getBytes("image");
                String description = resultSet.getString("description");
                int calories = resultSet.getInt("calories");
                double price = resultSet.getDouble("price");
                int categoryId = resultSet.getInt("category_id");

                Dish dish = new Dish(id, name, image, description, calories, price, categoryId);
                dishes.add(dish);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishes;
    }
    public List<Dish> getDishesByName(String menuName) {
        List<Dish> dishes = new ArrayList<>();

        String sql = "SELECT d.dish_id, d.name, d.part_image, d.description, d.calories, d.category_id, d.price " +
                "FROM dishes d " +
                "JOIN menu_dishes md ON d.dish_id = md.dish_id " +
                "JOIN menu m ON md.menu_id = m.menu_id " +
                "WHERE m.name = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, menuName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("dish_id");
                    String name = rs.getString("name");
                    byte[] image = rs.getBytes("part_image");
                    String description = rs.getString("description");
                    int calories = rs.getInt("calories");
                    int categoryId = rs.getInt("category_id");
                    double price = rs.getDouble("price");

                    Dish dish = new Dish(id, name, image, description, calories, price, categoryId);
                    dishes.add(dish);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dishes;
    }
    private boolean isDishInMenu(int menuId, int dishId) {
        String sql = "SELECT COUNT(*) AS count FROM menu_dishes WHERE menu_id = ? AND dish_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, menuId);
            statement.setInt(2, dishId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public int getDishIdByName(String dishName) {
        String sql = "SELECT dish_id FROM dishes WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dishName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("dish_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if dish with given name not found or error occurred
    }

    public List<String> getDishesInMenu(int menuId) {
        List<String> dishes = new ArrayList<>();
        String sql = "SELECT d.name FROM dishes d " +
                "JOIN menu_dishes md ON d.dish_id = md.dish_id " +
                "WHERE md.menu_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, menuId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String dishName = resultSet.getString("name");
                dishes.add(dishName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishes;
    }

    public void clearMenu(int menuId) {
        String sql = "DELETE FROM menu_dishes WHERE menu_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, menuId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}