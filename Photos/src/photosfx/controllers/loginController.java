package photosfx.controllers;

import java.io.File;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private ImageView Logo;

    public void loadImage() {
        Image image = new Image("file:///" + new File("Photos/data/userPhotos/LogoMain.png").getAbsolutePath().replace("\\", "/"));
        Logo.setImage(image);
    }

    /**
     * Attempts to log the user in when the login button is pressed.
     * It checks if the username matches "admin" for admin login or 
     * if it exists among regular users. If the username is valid, it transitions
     * to the appropriate view (admin or user dashboard).
     *
     * @param event The action event triggered by pressing the login button.
     */
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

    /**
     * Validates the provided username ensuring it is not empty or null.
     * 
     * @param username The username entered by the user.
     * @return True if the username is valid, false otherwise.
     */
    private boolean isValidUsername(String username) {
        return !username.trim().isEmpty();
    }

    /**
     * Loads the admin view/dashboard if the login is successful.
     */
    private void loadAdminView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/photosfx/view/admin.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setResizable(false);;
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Admin Dashboard");
            e.printStackTrace();
        }
    }

    /**
     * Loads the user view/dashboard for a successfully logged in user.
     * 
     * @param username The username of the logged-in user.
     */
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

    /**
     * Closes the login window.
     */
    private void closeLoginWindow() {
        stage.close();
    }
    
    /**
     * Displays an alert message to the user.
     * 
     * @param alertType The type of alert.
     * @param title The title of the alert.
     * @param message The message body of the alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sets the stage for this controller.
     * 
     * @param stage The primary stage of the application.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
