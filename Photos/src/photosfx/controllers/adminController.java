package photosfx.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import photosfx.models.Admin;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Admin Controls
 * @author Dhruv Shidhaye
 * @author Lakshya Gour
 */

public class adminController {
    @FXML
    private Button createUserButton;
    @FXML
    private Button logoutButton;
    @FXML
    private ListView<String> userListView;

    private ObservableList<String> userList;

    /**
 * Initializes the admin controller, populating the user list view with usernames.
 */
    public void initialize() {
        userList = FXCollections.observableArrayList(Admin.getUsernameList());
        userListView.setItems(userList);
        updateList();
    }

    /**
 * Updates the list view of users by fetching the latest usernames from the Admin model.
 */
    public void updateList() {
        userList.clear();
        userList.addAll(Admin.getUsernameList());
        userListView.setItems(userList);
        Admin.saveUsers("Photos/data/users.ser");

    }

/**
 * Creates a new user with the given username and updates the user list view.
 *
 * @param username The username of user. 
 */
    public void createUser(String username) {
        Admin.createUser(username);
        updateList();
    }

    /**
 * Deletes a user with the given username and updates the user list view.
 *
 * @param username The username of the user to delete.
 */
    public void deleteUser(String username) {
        Admin.deleteUser(username);
        //add functionality to delete the user's ser file here

        updateList();
    }


    /**
 * Displays a dialog box to input the username for a new user, then creates the user.
 */
    @FXML
    private void showCreateUserDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create User");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter Username:");
        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(
            new Image("file:///" + new File("Photos/data/userPhotos/LogoMain.png").getAbsolutePath().replace("\\", "/")));
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(username -> {
            if(username == null || username.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid username.");
            } else {
                createUser(username);
                showAlert(Alert.AlertType.INFORMATION, "User Created", "user: " + username + " created.");
            }
            
        });
    }


    /**
 * Displays a dialog box to select a user to delete, then deletes the selected user.
 */
    @FXML
    private void showDeleteUserDialog() {
        ObservableList<String> newUserList = FXCollections.observableArrayList(Admin.getUsernameList());
        //newUserList.remove("stock");
        ChoiceDialog<String> dialog = new ChoiceDialog<>(null, newUserList);
        dialog.setTitle("Delete User");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter Username:");
        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(
            new Image("file:///" + new File("Photos/data/userPhotos/LogoMain.png").getAbsolutePath().replace("\\", "/")));
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(username -> {
            if(username == null || username.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid username.");
                
            } else {
                showAlert(Alert.AlertType.INFORMATION, "User Created", "user: " + username + " deleted");
                
                deleteUser(username);
            }
        });
    }


    /**
 * Shows an alert dialog box with a specified message.
 *
 * @param alertType The type of the alert.
 * @param title     The title of the alert dialog.
 * @param message   The message to display in the alert dialog.
 */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    /**
 * Logs out the current user and returns to the login screen.
 */
    @FXML
    private void logout() {
        // Display confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                // Close the current stage
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                stage.close();
                // Load and display the login screen
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/photosfx/view/login.fxml"));
                    Parent root = loader.load();
                    Stage loginStage = new Stage();
                    loginStage.setTitle("Login");
                    loginStage.setScene(new Scene(root, 324.0, 158.0));
                    loginStage.setResizable(false);
                    loginStage.getIcons().add(new Image("file:///" + new File("Photos/data/userPhotos/LogoMain.png").getAbsolutePath().replace("\\", "/")));
                    loginController controller = loader.getController();
                    controller.setStage(loginStage);
                    controller.loadImage();
                    loginStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /* 
    @FXML
    private void showCreateUserDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/photosfx/view/createUserDialog.fxml"));
            VBox dialogPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(createUserButton.getScene().getWindow());
            dialogStage.setScene(new Scene(dialogPane));

            TextField usernameFieldInDialog = (TextField) dialogPane.lookup("#usernameField");
            Button createUserButtonInDialog = (Button) dialogPane.lookup("#createUserButton");
            createUserButtonInDialog.setOnAction(event -> {
                String username = usernameFieldInDialog.getText();
                if (username != null && !username.isEmpty()) {
                    
                    createUser(username);
                    dialogStage.close();
                } else {
                    // Show error message or handle invalid input
                    
                }
            });

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
    
}
