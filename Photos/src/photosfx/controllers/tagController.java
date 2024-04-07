package photosfx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import photosfx.models.Admin;
import photosfx.models.Photo;
import photosfx.models.Tags;
import photosfx.models.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class tagController {
    private static Stage stage;

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
    private static User loggedInUser;
    private static Photo selectedPhoto;

    @FXML
    private void initialize() {
        tagsObservableList = FXCollections.observableArrayList();
        tagKeysObservableList = FXCollections.observableArrayList();
        refreshTagsList();
        refreshTagKeysChoiceBox();
        // Default Tags Keys
        loggedInUser.addTagkey("Location");
        loggedInUser.addTagkey("Person");
        loggedInUser.addTagkey("Vibe");
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
    public static void setData(Photo photo, String username) {
        selectedPhoto = photo;
        loggedInUser = User.loadUser(username);
    }
    private void refreshTagsList() {
        tagsObservableList.clear();
        tagsObservableList.addAll(selectedPhoto.getTagsAsList());
        tagsListView.setItems(tagsObservableList);
        Admin.saveUsers("Photos/data/users.ser");
    }

    private void refreshTagKeysChoiceBox() {
        tagKeysObservableList.clear();
        Set<String> allTagKeys = loggedInUser.getAllTagKeys();
        tagKeysObservableList.addAll(allTagKeys);
        tagKeysChoiceBox.setItems(tagKeysObservableList);
        Admin.saveUsers("Photos/data/users.ser");
        }

    public void addTag() {
        String tagKey = tagKeysChoiceBox.getValue();
        String tagValueText = tagValueField.getText().trim();
        if (tagKey != null && !tagValueText.isEmpty()) {
            String[] tagValuesArray = tagValueText.split(",");
            for (String tagValue : tagValuesArray) {
                boolean allowMultipleValues = selectedPhoto.getTag().allowMultipleValues(tagKey);
                if (!allowMultipleValues && selectedPhoto.getTags().containsKey(tagKey)) {
                    Admin.showAlert(AlertType.ERROR, "Error", "Multiple values not allowed for this tag key.");
                    return;
                }
                selectedPhoto.addTag(tagKey, tagValue.strip(), allowMultipleValues);
            }
            Admin.saveUsers("Photos/data/users.ser");
            tagValueField.clear();
            refreshTagsList();
            refreshTagKeysChoiceBox();
            
        } else {
            Admin.showAlert(AlertType.ERROR, "Error", "Please select a tag key and enter a tag value");
        }
     }

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


    public void closeTagWindow() {
        stage.close();
    }

    public void setStage(Stage s) {
        stage = s;
    }

    public void addTagKey() {
        String tagKey = tagKeyField.getText().trim();
        if(!tagKey.isEmpty() && !tagKeysObservableList.contains(tagKey)) {
            loggedInUser.addTagkey(tagKey);
            Admin.saveUsers("Photos/data/users.ser");
            refreshTagKeysChoiceBox();
            tagKeyField.clear();
        } else {
            Admin.showAlert(AlertType.ERROR, "Error", "Please enter a tag key");
        }
    }

}
