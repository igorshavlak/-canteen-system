package org.example.coursework.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.coursework.entities.Dish;
import org.example.coursework.repositories.CategoryRepository;
import org.example.coursework.repositories.DishRepository;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


public class MenuController {
    @FXML
    private TableView<Dish> canteenMenuTable;

    @FXML
    private TableColumn<Dish, ImageView> canteenImage;

    @FXML
    private TableColumn<Dish, String> canteenName;

    @FXML
    private TableColumn<Dish, Double> canteenPrice;

    @FXML
    private TableColumn<Dish, String> canteenDesc;

    @FXML
    private TableColumn<Dish, Integer> canteenCategory;

    @FXML
    private TableColumn<Dish, Integer> canteenCalories;

    @FXML
    private ImageView imageView;
    @FXML
    private Button uploadButton;
    private DishRepository dishRepository;
    private CategoryRepository categoryRepository;
    @FXML
    private TextField dishName;

    @FXML
    private TextField dishPrice;

    @FXML
    private TextField dishDesc;


    @FXML
    private TextField dishCalories;
    private byte[] dishImage;
    @FXML
    private ComboBox<String> categoryComboBox;

    ObservableList<Dish> canteenMenuData;

    @FXML
    private void initialize() {

        canteenImage.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getImageView()));

        canteenName.setCellValueFactory(new PropertyValueFactory<Dish,String>("name"));
        canteenPrice.setCellValueFactory(new PropertyValueFactory<Dish,Double>("price"));
        canteenDesc.setCellValueFactory(new PropertyValueFactory<Dish,String>("description"));
        canteenCategory.setCellValueFactory(new PropertyValueFactory<Dish,Integer>("categoryId"));
        canteenCalories.setCellValueFactory(new PropertyValueFactory<Dish,Integer>("calories"));
        loadCategories();
        loadDishesFromDatabase();
        canteenMenuTable.setItems(canteenMenuData);


    }
    private void loadCategories() {
        categoryRepository = new CategoryRepository();
        List<String> categories = categoryRepository.getAllCategoriesNames(); // Метод для получения списка категорий из репозитория
        categoryComboBox.setItems(FXCollections.observableArrayList(categories));
    }
    @FXML
    private void handleNavigateToSlotButton(ActionEvent event) throws IOException {
        Parent slotPage = FXMLLoader.load(getClass().getResource("/org/example/coursework/slot.fxml"));
        Scene slotPageScene = new Scene(slotPage);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(slotPageScene);
        window.show();


    }

        private void loadDishesFromDatabase() {
        dishRepository = new DishRepository();
        categoryRepository = new CategoryRepository();
        List<Dish> list = dishRepository.getAllDishes();
        for (Dish dish : list) {
            if (dish.getImage() != null) {
                ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(dish.getImage())));
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                dish.setImageView(imageView);
            }
        }
        canteenMenuData = FXCollections.observableArrayList(list);

    }
    @FXML
    private void handleAddButton(ActionEvent event) {
        String name = dishName.getText().trim();
        double price = Double.parseDouble(dishPrice.getText().trim());
        String description = dishDesc.getText().trim();
        String category = categoryComboBox.getValue();
        int calories = Integer.parseInt(dishCalories.getText().trim());


        dishRepository = new DishRepository();
        categoryRepository = new CategoryRepository();
        dishRepository.createDish(new Dish(name,dishImage,description, calories, price, categoryRepository.getCategoryIdByName(category)));
        clearFields();
    }

    private void clearFields() {
        dishName.clear();
        dishPrice.clear();
        dishDesc.clear();
        categoryComboBox.getSelectionModel().clearSelection();
        dishCalories.clear();
    }

    @FXML
    private void handleUploadButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());

        if (file != null) {
            try {
                Image image = new Image(new FileInputStream(file));
                imageView.setImage(image);

                byte[] imageBytes = new byte[(int) file.length()];
                try (FileInputStream fis = new FileInputStream(file)) {
                    fis.read(imageBytes);
                }
                dishImage = imageBytes;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleRemoveButton(ActionEvent event) {
        // Получаем выбранное блюдо
        Dish selectedDish = canteenMenuTable.getSelectionModel().getSelectedItem();

        if (selectedDish != null) {
            // Удаляем блюдо из базы данных
            dishRepository = new DishRepository();
            dishRepository.deleteDish(selectedDish.getId()); // Предположим, у блюда есть ID

            // Удаляем блюдо из таблицы
            canteenMenuData.remove(selectedDish);
        } else {
            // Покажите сообщение, если блюдо не выбрано
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please, select a dish");
            alert.showAndWait();
        }
    }

}
