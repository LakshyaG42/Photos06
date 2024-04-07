package photosfx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import photosfx.models.Admin;
import photosfx.models.Album;
import photosfx.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class userController {
    @FXML
    private TextField albumNameField;

    @FXML
    private ListView<String> albumListView;

    @FXML private Button logoutButton;

    private static String username;
    private User loggedInUser;

    public static void initData(String u) {
        username = u;
    }
    @FXML
    private void initialize() {
        loggedInUser = User.loadUser(username);
        refreshAlbumList();
    }
    
    private void refreshAlbumList() {
        ArrayList<String> albumNames = new ArrayList<>();
        for (Album album : loggedInUser.getAlbums()) {
            albumNames.add(album.getName());
        }
        albumListView.getItems().clear();
        albumListView.getItems().addAll(albumNames);
    }

    @FXML
    private void createAlbum() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Album");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter Album:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String albumName = result.get().trim();
            if (!albumName.isEmpty()) {
            loggedInUser.addAlbum(new Album(albumName));
            Admin.saveUsers("Photos/data/users.ser");
            refreshAlbumList();
            } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid album name.");
            }
        }
    }

    @FXML
    private void deleteAlbum() {
        String selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            for (Album album : loggedInUser.getAlbums()) {
                if (album.getName().equals(selectedAlbum)) {
                    loggedInUser.removeAlbum(album);
                    break;
                }
            }
            Admin.saveUsers("Photos/data/users.ser");
            refreshAlbumList();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an album to delete.");
        }
    }

    @FXML
    private void renameAlbum() {
        String selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            TextInputDialog dialog = new TextInputDialog(selectedAlbum);
            dialog.setTitle("Renaming Album: " + selectedAlbum);
            dialog.setHeaderText(null);
            dialog.setContentText("Enter the new name for the album:");
    
            // Show the text input dialog and wait for user input
            dialog.showAndWait().ifPresent(newAlbumName -> {
                newAlbumName = newAlbumName.trim();
                if (!newAlbumName.isEmpty()) {
                    for (Album album : loggedInUser.getAlbums()) {
                        if (album.getName().equals(selectedAlbum)) {
                            loggedInUser.renameAlbum(album, newAlbumName);
                            break;
                        }
                    }
                    Admin.saveUsers("Photos/data/users.ser");
                    refreshAlbumList();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid new album name.");
                }
            });
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an album to rename.");
        }
    }

    @FXML
    private void openAlbum() {
        String selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            for (Album album : loggedInUser.getAlbums()) {
                if (album.getName().equals(selectedAlbum)) {
                    loadAlbumView(album);
                    break;
                }
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an album to open.");
        }
    }



    private void loadAlbumView(Album album) {
        try {
            albumController.initData(album, username); // Pass the data to the album controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/photosfx/view/album.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("User Dashboard");
            albumController.setStage(stage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(albumListView.getScene().getWindow());
            stage.show();
            stage.setOnHidden(e -> refreshAlbumView());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load User Dashboard");
            e.printStackTrace();
        }
    }

    public void refreshAlbumView() {
        loggedInUser = User.loadUser(username);
        refreshAlbumList();
    }

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
                    loginStage.setScene(new Scene(root, 400, 300));
                    loginController controller = loader.getController();
                    controller.setStage(loginStage);
                    loginStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

}
