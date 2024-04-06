package photosfx.controllers;

import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import photosfx.models.Admin;
import photosfx.models.User;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    }

    public void createUser(String username) {
        Admin.createUser(username);
        updateList();
    }

    public void deleteUser(String username) {
        Admin.deleteUser(username);
        updateList();
    }

    @FXML
    private void showCreateUserDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create User");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter Username:");

        // Show the dialog box and wait for user input
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(username -> {
            if(username == null || username.isEmpty()) {
                // Show error message or handle invalid input
                System.out.println("Please enter a valid username.");
            } else {
                System.out.println("user: " + username + " created.");
                createUser(username);
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
