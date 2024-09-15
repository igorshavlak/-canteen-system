package org.example.coursework.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.coursework.entities.User;
import org.example.coursework.repositories.UserRepository;


public class LoginController {

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Text invalid;

    @FXML
    private void loginCheck() {
        String login = username.getText();
        String pass = password.getText();

        User user = UserRepository.login(login, pass);
        if (user != null) {
            invalid.setVisible(false);
            try {
                Stage stage = (Stage) username.getScene().getWindow();
                redirectToRolePage(user, stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            invalid.setVisible(true);
        }
    }
    @FXML
    private void redirectToRolePage(User user, Stage stage) throws Exception{
        String role = user.getRole();
        Parent root = switch (role) {
            case "admin" -> FXMLLoader.load(getClass().getResource("/org/example/coursework/menu.fxml"));
            case "student" -> FXMLLoader.load(getClass().getResource("/org/example/coursework/canteen.fxml"));
            case "waiter" -> FXMLLoader.load(getClass().getResource("/org/example/coursework/transaction.fxml"));
            default -> throw new Exception("Unknown role");
        };

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
