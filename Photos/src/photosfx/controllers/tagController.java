package photosfx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import photosfx.models.Admin;
import photosfx.models.Album;
import photosfx.models.Photo;
import photosfx.models.User;

import java.util.Set;

public class tagController {
    private static Stage stage3;

    @FXML
    private CheckBox allowMultipleValuesCheckBox;

    @FXML 
    private ListView<String> tagsListView;

    @FXML
    private ChoiceBox<String> tagKeysChoiceBox;

    @FXML
    private TextField tagValueField;

    @FXML
    private TextField tagKeyField;

    @FXML
    private Button addTagButton;

    @FXML
    private Button addTagTypeButton;

    @FXML
    private Button deleteTagButton;

    private ObservableList<String> tagsObservableList;
    private ObservableList<String> tagKeysObservableList;
    private static String username;
    private static String selectedPhotofilename;
    private static String inputAlbumName;
    private User loggedInUser;
    private Photo selectedPhoto;
    private Album currentAlbum;

    /**
     * Initializes the controller class.
     */
    @FXML
    private void initialize() {
        loggedInUser = User.loadUser(username);
        currentAlbum = loggedInUser.getAlbum(inputAlbumName);
        for (Photo photo : currentAlbum.getPhotos()) {
            if (photo.getFilePath().equals(selectedPhotofilename)) {
                selectedPhoto = photo;
                break;
            }
        }
        tagsObservableList = FXCollections.observableArrayList();
        tagKeysObservableList = FXCollections.observableArrayList();
        refreshTagsList();
        refreshTagKeysChoiceBox();
        // Default Tags Keys
        
        loggedInUser.addTagkey("Location", false);
        loggedInUser.addTagkey("Person", true);
        loggedInUser.addTagkey("Vibe", true);
        tagsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deleteTagButton.setDisable(newValue == null); 
        });

        addTagButton.disableProperty().bind(
            tagKeysChoiceBox.valueProperty().isNull()
            .or(tagValueField.textProperty().isEmpty())
        );
        addTagTypeButton.disableProperty().bind(
            tagKeyField.textProperty().isEmpty()
        );
    }

     /**
     * Sets the necessary data for the tag controller to operate upon.
     *
     * @param photo The file path of the photo to be managed.
     * @param u The username of the logged-in user.
     * @param albumName The name of the album containing the photo.
     */
    public static void setData(String photo, String u, String albumName) {
        selectedPhotofilename = photo;
        username = u;
        inputAlbumName = albumName;

    }

    /**
     * Refreshes the list of tags displayed in the UI.
     */
    private void refreshTagsList() {
        tagsObservableList.clear();
        tagsObservableList.addAll(selectedPhoto.getTagsAsList());
        tagsListView.setItems(tagsObservableList);
        Admin.saveUsers("Photos/data/users.ser");
    }

    /**
     * Refreshes the choice box containing the available tag keys.
     */
    private void refreshTagKeysChoiceBox() {
        tagKeysObservableList.clear();
        Set<String> allTagKeys = loggedInUser.getAllTagKeys();
        tagKeysObservableList.addAll(allTagKeys);
        tagKeysChoiceBox.setItems(tagKeysObservableList);
        Admin.saveUsers("Photos/data/users.ser");
        }

    /**
     * Handles adding a new tag to the selected photo based on the user input.
     */
    public void addTag() {
        String tagKey = tagKeysChoiceBox.getValue();
        String tagValueText = tagValueField.getText().trim();
        Boolean allowMulti = loggedInUser.isallowMultipleValues(tagKey);
        if (tagKey != null && !tagValueText.isEmpty()) {
            String[] tagValuesArray = tagValueText.split(",");
            for (String tagValue : tagValuesArray) {
                if (!allowMulti && selectedPhoto.getTags().containsKey(tagKey)) {
                    Admin.showAlert(AlertType.ERROR, "Error", "Multiple values not allowed for this tag key.");
                    return;
                }
                selectedPhoto.addTag(tagKey, tagValue.strip(), allowMulti);
            }
            Admin.saveUsers("Photos/data/users.ser");
            tagValueField.clear();
            refreshTagsList();
            refreshTagKeysChoiceBox();
            
        } else {
            Admin.showAlert(AlertType.ERROR, "Error", "Please select a tag key and enter a tag value");
        }
     }


     /**
     * Handles the deletion of a selected tag from the selected photo.
     */
    public void deleteTag() {
        String selectedTag = tagsListView.getSelectionModel().getSelectedItem();
        if (selectedTag != null) {
            String[] parts = selectedTag.split(": ");
            if (parts.length == 2) {
            selectedPhoto.removeTag(parts[0], parts[1]);
            refreshTagsList();
            refreshTagKeysChoiceBox();
            }
        } else {
            Admin.showAlert(AlertType.ERROR, "Error", "Please select a tag to delete");
        }
    }

    /**
     * Closes the tag management window.
     */
    public void closeTagWindow() {
        stage3.close();
    }

    /**
     * Sets the stage of this controller to the given stage.
     * 
     * @param s The stage for this controller.
     */
    public void setStage(Stage s) {
        stage3 = s;
    }

    /**
     * Handles adding a new tag key to the system.
     */
    public void addTagKey() {
        boolean allowMultipleValues = allowMultipleValuesCheckBox.isSelected();
        String tagKey = tagKeyField.getText().trim();
        if(!tagKey.isEmpty() && !tagKeysObservableList.contains(tagKey)) {
            loggedInUser.addTagkey(tagKey, allowMultipleValues);
            Admin.saveUsers("Photos/data/users.ser");
            refreshTagKeysChoiceBox();
            tagKeyField.clear();
        } else {
            Admin.showAlert(AlertType.ERROR, "Error", "Please enter a tag key");
        }
    }

}
