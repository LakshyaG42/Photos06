package photosfx.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import photosfx.models.Admin;
import javafx.scene.control.Alert.AlertType;

/**
 * Login Controls
 * @author Dhruv Shidhaye
 * @author Lakshya Gour
 */

public class loginController {
    @FXML
    private TextField usernameField;

    @FXML
    private Stage stage;

    @FXML
    void login(ActionEvent event) {
        String username = usernameField.getText().trim();
        boolean userfound = false;
        if (isValidUsername(username)) {
            //AdminController Call
            if (username.equals("admin")) {
                userfound = true;
                closeLoginWindow();
                loadAdminView();
            } else {
                // User Controller Call
                for (String user : Admin.getUsernameList()) {
                    if (user.equals(username)) {
                        userfound = true;
                        closeLoginWindow();
                        loadUserView(username);
                        //showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
                    }
                }
            }
            if(userfound == false) {showAlert(Alert.AlertType.ERROR, "Error", "User doesn't exist, please enter a valid username");}
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Username, please enter a valid username");
        }
    }

    private boolean isValidUsername(String username) {
        return !username.trim().isEmpty();
    }

    private void loadAdminView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/photosfx/view/admin.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Admin Dashboard");
            e.printStackTrace();
        }
    }

    private void loadUserView(String username) {
        try {
            userController.initData(username); // Pass the username to the user controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/photosfx/view/user.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("User Dashboard");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load User Dashboard");
            e.printStackTrace();
        }
    }


    private void closeLoginWindow() {
        stage.close();
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
