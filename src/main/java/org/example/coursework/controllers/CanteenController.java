package org.example.coursework.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import org.example.coursework.DbConnection;
import org.example.coursework.entities.Cart;
import org.example.coursework.entities.Dish;
import org.example.coursework.entities.OrderItem;
import org.example.coursework.repositories.DishRepository;
import org.example.coursework.repositories.MenuRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;
import java.util.Random;

public class CanteenController {
    @FXML
    private TableView<OrderItem> CartTab;
    @FXML
    private TableColumn<OrderItem, ImageView> imageCart; // Updated type to ImageView
    @FXML
    private TableColumn<OrderItem, String> dishCart;
    @FXML
    private TableColumn<OrderItem, Integer> quantityCart;
    @FXML
    private TableColumn<OrderItem, Integer> caloriesCart;
    @FXML
    private TableColumn<OrderItem, Double> priceCart;

    @FXML
    private TableView<Dish> MenuTab;
    @FXML
    private TableColumn<Dish, ImageView> imageMenu;
    @FXML
    private TableColumn<Dish, String> dishMenu;
    @FXML
    private TableColumn<Dish, Integer> quantityMenu;
    @FXML
    private TableColumn<Dish, Integer> caloriesMenu;
    @FXML
    private TableColumn<Dish, Double> priceMenu;

    @FXML
    private ComboBox<Dish> item;
    @FXML
    private TextField quantity;

    @FXML
    private RadioButton upi;
    @FXML
    private RadioButton ewallet;
    @FXML
    private RadioButton cash;

    @FXML
    private TextField orderID;
    @FXML
    private TextField amount;
    @FXML
    private TextField status;
    @FXML
    private Label selectOneOption;

    private DishRepository dishRepository;
    private MenuRepository menuRepository;
    private Cart cart;
    private int generatedOrderId;

    public CanteenController() {
        this.menuRepository = new MenuRepository();
        this.cart = new Cart();
        this.dishRepository = new DishRepository();
    }

    @FXML
    public void initialize() {
        // Initialize columns for CartTab
        imageCart.setCellValueFactory(cellData -> {
            Dish dish = cellData.getValue().getDish();
            dish.setImageViewFromBytes(); // Ensure ImageView is created
            return new SimpleObjectProperty<>(dish.getImageView());
        });
        dishCart.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDish().getName()); // Use the dish's name
        });
        quantityCart.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        caloriesCart.setCellValueFactory(cellData -> {
            return new SimpleObjectProperty<>(cellData.getValue().getDish().getCalories());
        });
        priceCart.setCellValueFactory(cellData -> {
            return new SimpleObjectProperty<>(cellData.getValue().getDish().getPrice());
        });

        generatedOrderId = generateRandomOrderId();
        orderID.setText(String.valueOf(generatedOrderId));
        // Initialize columns for MenuTab
        imageMenu.setCellValueFactory(cellData -> {
            Dish dish = cellData.getValue();
            dish.setImageViewFromBytes();
            return new SimpleObjectProperty<>(dish.getImageView());
        });
        dishMenu.setCellValueFactory(new PropertyValueFactory<>("name"));
        caloriesMenu.setCellValueFactory(new PropertyValueFactory<>("calories"));
        priceMenu.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Load menu based on current time
        loadMenuByTime();

        // Initialize ComboBox for selecting dishes
        List<Dish> allDishes = dishRepository.getAllDishes();
        item.getItems().addAll(allDishes);
        item.setConverter(new StringConverter<Dish>() {
            @Override
            public String toString(Dish object) {
                return object.getName();
            }

            @Override
            public Dish fromString(String string) {
                return null;  // Not needed for ComboBox
            }
        });

        // Set up payment method toggle group
        ToggleGroup paymentMethodGroup = new ToggleGroup();
        upi.setToggleGroup(paymentMethodGroup);
        ewallet.setToggleGroup(paymentMethodGroup);
        cash.setToggleGroup(paymentMethodGroup);
    }

    private void loadMenuByTime() {
        // Determine current time and load menu accordingly
        int currentHour = java.time.LocalTime.now().getHour();
        if (currentHour >= 7 && currentHour < 10) {
            loadMenu("breakfast");
        } else if (currentHour >= 12 && currentHour < 15) {
            loadMenu("lunch");
        } else if (currentHour >= 17 && currentHour < 20) {
            loadMenu("dinner");
        } else {
            // Default to Breakfast menu if time not within defined intervals
            loadMenu("breakfast");
        }
    }

    private void loadMenu(String menuName) {
        List<Dish> menuItems = menuRepository.getDishesByName(menuName);
        MenuTab.getItems().clear(); // Clear existing items
        MenuTab.getItems().addAll(menuItems); // Add new menu items
    }

    @FXML
    private void handleAddButton(ActionEvent event) {
        Dish selectedDish = item.getValue();
        int selectedQuantity = Integer.parseInt(quantity.getText());

        // Add selected dish to cart
        cart.addItem(selectedDish, selectedQuantity);

        // Refresh cart table view
        refreshCartTableView();
    }
    @FXML
    private void handleRemoveButton(ActionEvent event) {
        // Remove selected item from cart
        OrderItem selectedItem = CartTab.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            cart.removeItem(selectedItem.getDish());
            refreshCartTableView();
        }
    }

    @FXML
    private void handleUpdateButton(ActionEvent event) {
        // Update selected item quantity in cart
        OrderItem selectedItem = CartTab.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            int newQuantity = Integer.parseInt(quantity.getText());
            selectedItem.setQuantity(newQuantity);
            refreshCartTableView();
        }
    }

    private void refreshCartTableView() {
        CartTab.getItems().clear(); // Clear existing items
        CartTab.getItems().addAll(cart.getItems()); // Add updated cart items
        amount.setText(String.format("Total: %.2f", cart.getTotalPrice())); // обновляем отображение суммы
    }

    @FXML
    private void handlePayButton(ActionEvent event) {
        String paymentMethod = "";
        if (upi.isSelected()) {
            paymentMethod = "UPI";
        } else if (ewallet.isSelected()) {
            paymentMethod = "E-Wallet";
        } else if (cash.isSelected()) {
            paymentMethod = "Cash";
        }

        if (paymentMethod.isEmpty()) {
            selectOneOption.setText("Please select a payment option.");
        } else {


            // Подсчет общей суммы заказа
            double totalAmount = cart.getTotalPrice();
            amount.setText(String.valueOf(totalAmount));

            // Сохранение информации о заказе в базу данных
            saveOrderToDatabase(generatedOrderId, totalAmount);
            status.setText("Order placed. Payment method: " + paymentMethod);

        }
    }
    private Integer generateRandomOrderId() {

        Random random = new Random();

        return random.nextInt(999999);
    }
    public  void saveOrderToDatabase(int orderId, double totalAmount) {
        String insertOrderQuery = "INSERT INTO orders (order_id, date, total_price, status) VALUES (?, ?, ?, ?)";
        String insertOrderDishQuery = "INSERT INTO order_dishes (order_id, dish_id) VALUES (?, ?)";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement orderStatement = connection.prepareStatement(insertOrderQuery);
             PreparedStatement orderDishStatement = connection.prepareStatement(insertOrderDishQuery)) {


            orderStatement.setInt(1, orderId);
            orderStatement.setDate(2,  new Date(System.currentTimeMillis()));
            orderStatement.setDouble(3, totalAmount);
            orderStatement.setBoolean(4, true);
            orderStatement.executeUpdate();


            for (OrderItem item : cart.getItems()) {
                orderDishStatement.setInt(1, orderId);
                orderDishStatement.setInt(2, item.getDish().getId());
                orderDishStatement.executeUpdate();
            }

            status.setText("Order placed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            status.setText("Failed to place the order.");
        }
    }

}
