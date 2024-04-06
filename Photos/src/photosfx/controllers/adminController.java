package photosfx.controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import photosfx.models.Admin;
import photosfx.models.User;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class adminController {
    @FXML
    private Button createUserButton;
    @FXML
    private ListView<String> userListView;

    private ObservableList<String> userList;

    public void initialize() {
        userList = FXCollections.observableArrayList(Admin.getUsernameList());
        userListView.setItems(userList);
        updateList();
    }

    public void updateList() {
        userList.clear();
        userList.addAll(Admin.getUsernameList());
        userListView.setItems(userList);
        Admin.saveUsers("Photos/data/users.ser");

    }

    public void createUser(String username) {
        Admin.createUser(username);
        updateList();
    }

    public void deleteUser(String username) {
        Admin.deleteUser(username);
        //add functionality to delete the user's ser file here

        updateList();
    }

    @FXML
    private void showCreateUserDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create User");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter Username:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(username -> {
            if(username == null || username.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid username.");
                System.out.println("Please enter a valid username.");
            } else {
                createUser(username);
                System.out.println("user: " + username + " created.");
                showAlert(Alert.AlertType.INFORMATION, "User Created", "user: " + username + " created.");
            }
            
        });
    }

    @FXML
    private void showDeleteUserDialog() {
        ObservableList<String> newUserList = FXCollections.observableArrayList(Admin.getUsernameList());
        newUserList.remove("stock");
        ChoiceDialog<String> dialog = new ChoiceDialog<>(null, newUserList);
        dialog.setTitle("Delete User");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter Username:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(username -> {
            if(username == null || username.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid username.");
                System.out.println("Please enter a valid username.");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "User Created", "user: " + username + " deleted");
                System.out.println("user: " + username + " deleted.");
                deleteUser(username);
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
                    System.out.println("Creating user: " + username);
                    createUser(username);
                    dialogStage.close();
                } else {
                    // Show error message or handle invalid input
                    System.out.println("Please enter a valid username.");
                }
            });

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
    
}
