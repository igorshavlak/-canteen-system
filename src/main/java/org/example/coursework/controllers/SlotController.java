package org.example.coursework.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.coursework.repositories.DishRepository;
import org.example.coursework.repositories.MenuRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SlotController {

    @FXML
    private ComboBox<String> dishNameComboBox;
    @FXML
    private CheckBox breakfastchk;
    @FXML
    private CheckBox lunchchk;
    @FXML
    private CheckBox dinnerchk;
    @FXML
    private TextArea breakfastSlot;
    @FXML
    private TextArea lunchSlot;
    @FXML
    private TextArea dinnerSlot;

    private DishRepository dishRepository;
    private MenuRepository menuRepository;

    @FXML
    private void initialize() {
        dishRepository = new DishRepository();
        loadDishNames();
        menuRepository = new MenuRepository();
        setupCheckboxListeners();
        loadMenus();

    }

    private void loadDishNames() {
        List<String> dishNames = dishRepository.getDishNames();
        ObservableList<String> dishNameList = FXCollections.observableArrayList(dishNames);
        dishNameComboBox.setItems(dishNameList);
    }
    private void loadMenus() {
        Map<String, List<String>> menus = menuRepository.getAllMenus();

        List<String> breakfastDishes = menus.get("Breakfast");
        List<String> lunchDishes = menus.get("Lunch");
        List<String> dinnerDishes = menus.get("Dinner");

        displayMenuInTextArea(breakfastDishes, breakfastSlot);
        displayMenuInTextArea(lunchDishes, lunchSlot);
        displayMenuInTextArea(dinnerDishes, dinnerSlot);
    }
    private void displayMenuInTextArea(List<String> dishes, TextArea textArea) {
        StringBuilder sb = new StringBuilder();
        for (String dish : dishes) {
            sb.append(dish).append("\n");
        }
        textArea.setText(sb.toString());
    }

    private void setupCheckboxListeners() {
        breakfastchk.setOnAction(event -> {
            if (breakfastchk.isSelected()) {
                lunchchk.setSelected(false);
                dinnerchk.setSelected(false);
            }
        });

        lunchchk.setOnAction(event -> {
            if (lunchchk.isSelected()) {
                breakfastchk.setSelected(false);
                dinnerchk.setSelected(false);
            }
        });

        dinnerchk.setOnAction(event -> {
            if (dinnerchk.isSelected()) {
                breakfastchk.setSelected(false);
                lunchchk.setSelected(false);
            }
        });
    }

    @FXML
    private void handleAddButton() {
        String selectedDishName = dishNameComboBox.getValue();
        if (selectedDishName == null || selectedDishName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "No Dish Selected", "Please select a dish to add.");
            return;
        }

        int menuId = -1;
        if (breakfastchk.isSelected()) {
            menuId = 1; // Assuming menu_id 1 corresponds to Breakfast
        } else if (lunchchk.isSelected()) {
            menuId = 2; // Assuming menu_id 2 corresponds to Lunch
        } else if (dinnerchk.isSelected()) {
            menuId = 3; // Assuming menu_id 3 corresponds to Dinner
        } else {
            showAlert(Alert.AlertType.ERROR, "No Slot Selected", "Please select one slot (Breakfast, Lunch, or Dinner) to add the dish.");
            return;
        }

        int dishId = menuRepository.getDishIdByName(selectedDishName);
        if (dishId == -1) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to get dish ID.");
            return;
        }

        boolean added = menuRepository.addDishToMenu(menuId, dishId);
        if (added) {
            TextArea selectedSlotTextArea = determineSelectedSlotTextArea();
            if (selectedSlotTextArea != null) {
                selectedSlotTextArea.appendText(selectedDishName + "\n");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add dish to menu.");
        }
    }

    private TextArea determineSelectedSlotTextArea() {
        if (breakfastchk.isSelected()) {
            return breakfastSlot;
        } else if (lunchchk.isSelected()) {
            return lunchSlot;
        } else if (dinnerchk.isSelected()) {
            return dinnerSlot;
        } else {
            return null;
        }
    }
    private int getSelectedMenuId() {
        if (breakfastchk.isSelected()) {
            return 1; // Пример: завтрак
        } else if (lunchchk.isSelected()) {
            return 2; // Пример: обед
        } else if (dinnerchk.isSelected()) {
            return 3; // Пример: ужин
        } else {
            return -1; // Обработка ошибки: не выбрано ни одно из меню
        }
    }
    @FXML
    private void handleClearButton() {
        switch (getSelectedMenuId()) {
            case 1:
                breakfastSlot.clear();
                break;
            case 2:
                lunchSlot.clear();
                break;
            case 3:
                dinnerSlot.clear();
                break;
            default:
                // Handle unknown menuId
                break;
        }

        // Очистка ComboBox с именем блюда
        dishNameComboBox.setValue(null);

        // Очистка таблицы menu_dishes в базе данных
        menuRepository.clearMenu(getSelectedMenuId());
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleBackButton(ActionEvent actionEvent) throws IOException {
        Parent slotPage = FXMLLoader.load(getClass().getResource("/org/example/coursework/menu.fxml"));
        Scene slotPageScene = new Scene(slotPage);

        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(slotPageScene);
        window.show();
    }
}
