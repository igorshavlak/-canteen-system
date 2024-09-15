package org.example.coursework.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.coursework.DbConnection;
import org.example.coursework.entities.Dish;
import org.example.coursework.entities.Order;
import org.example.coursework.repositories.DishRepository;


import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.List;

public class TransactionController {

    @FXML
    private TextField orderID;
    @FXML
    private TextField amount;
    @FXML
    private CheckBox paid;

    @FXML
    private TableView<Order> TranTab;
    @FXML
    private TableColumn<Order, Integer> OrderID;
    @FXML
    private TableColumn<Order, Double> Amount;
    @FXML
    private TableColumn<Order, Boolean> Status;

    private ObservableList<Order> ordersData = FXCollections.observableArrayList();
    DishRepository dishRepository = new DishRepository();


    @FXML
    public void initialize() {
        // Настройка столбцов таблицы
        OrderID.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        Amount.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        Status.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Загрузка данных из базы данных
        loadOrdersFromDatabase();

        // Добавление слушателя для выбора строки в таблице
        TranTab.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectOrder(newValue);
        });
    }

    private void loadOrdersFromDatabase() {
        ordersData.clear();

        String query = "SELECT order_id, date, total_price, status FROM orders";
        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                Date date = rs.getDate("date");
                double totalPrice = rs.getDouble("total_price");
                boolean status = rs.getBoolean("status");

                Order order = new Order(orderId, date, totalPrice, status);
                ordersData.add(order);
            }

            TranTab.setItems(ordersData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void selectOrder(Order order) {
        if (order != null) {
            orderID.setText(String.valueOf(order.getOrderId()));
            amount.setText(String.valueOf(order.getTotalPrice()));
            paid.setSelected(order.isStatus());
            showOrderDetails(order.getOrderId());
        } else {
            orderID.clear();
            amount.clear();
            paid.setSelected(false);
        }
    }

    @FXML
    private void handleAddButton() {
        // Добавление нового заказа
        try {
            double newAmount = Double.parseDouble(amount.getText());
            boolean newStatus = paid.isSelected();

            String query = "INSERT INTO orders (date, total_price, status) VALUES (?, ?, ?)";

            try (Connection connection = DbConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
                stmt.setDouble(2, newAmount);
                stmt.setBoolean(3, newStatus);

                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating order failed, no rows affected.");
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newOrderId = generatedKeys.getInt(1);
                        // Обновление таблицы
                        loadOrdersFromDatabase();
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            // Обработка ошибки ввода числа
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRemoveButton() {
        // Удаление выбранного заказа
        Order selectedOrder = TranTab.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            int orderIdToDelete = selectedOrder.getOrderId();

            String query = "DELETE FROM orders WHERE order_id = ?";

            try (Connection connection = DbConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(query)) {

                stmt.setInt(1, orderIdToDelete);
                stmt.executeUpdate();

                // Обновление таблицы
                loadOrdersFromDatabase();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleUpdateButton() {
        // Обновление выбранного заказа
        Order selectedOrder = TranTab.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            try {
                int updatedOrderId = selectedOrder.getOrderId();
                double updatedAmount = Double.parseDouble(amount.getText());
                boolean updatedStatus = paid.isSelected();

                String query = "UPDATE orders SET total_price = ?, status = ? WHERE order_id = ?";

                try (Connection connection = DbConnection.getConnection();
                     PreparedStatement stmt = connection.prepareStatement(query)) {

                    stmt.setDouble(1, updatedAmount);
                    stmt.setBoolean(2, updatedStatus);
                    stmt.setInt(3, updatedOrderId);

                    stmt.executeUpdate();

                    // Обновление таблицы
                    loadOrdersFromDatabase();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } catch (NumberFormatException e) {
                // Обработка ошибки ввода числа
                e.printStackTrace();
            }
        }
    }
    // Этот метод вызывается при нажатии на транзакцию
    public void showOrderDetails(int orderId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/coursework/modal.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            // Получаем контроллер модального окна
            ModalController controller = loader.getController();

            // Получаем данные о блюдах заказа из базы данных
            List<Dish> dishes = dishRepository.getOrderDishes(orderId);

            // Передаем данные в модальное окно
            controller.setDishesData(dishes);

            // Устанавливаем модальность
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Order Details");
            stage.showAndWait();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Пример метода для получения блюд из базы данных
    private List<Dish> getOrderDishes(int orderId) {
        // Логика для получения блюд заказа по orderId (например, через запрос к базе данных)
        // Вернуть List<Dish>
        return List.of(); // Заглушка
    }
}
