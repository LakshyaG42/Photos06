package photosfx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class loginController {
    @FXML
    private TextField usernameField;

    @FXML
    void login(ActionEvent event) {
        String username = usernameField.getText();
        if (isValidUsername(username)) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
            //AdminController Call
            




        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Username, please enter a valid username");
        }
    }

    private boolean isValidUsername(String username) {
        return !username.trim().isEmpty();
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
