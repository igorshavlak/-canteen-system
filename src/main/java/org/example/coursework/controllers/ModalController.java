package org.example.coursework.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.coursework.entities.Dish;

import java.util.List;

public class ModalController {
    @FXML
    private TableView<Dish> dishesTable;

    @FXML
    private TableColumn<Dish, String> nameColumn;

    @FXML
    private TableColumn<Dish, String> descriptionColumn;

    @FXML
    private TableColumn<Dish, Integer> caloriesColumn;

    @FXML
    private TableColumn<Dish, Double> priceColumn;

    // Метод для инициализации данных
    public void initialize() {

        nameColumn.setCellValueFactory(new PropertyValueFactory<Dish,String>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Dish,String>("description"));
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<Dish,Integer>("calories"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Dish,Double>("price"));
    }

    // Метод для передачи данных в таблицу
    public void setDishesData(List<Dish> dishes) {
        dishesTable.getItems().setAll(dishes);
    }

}
