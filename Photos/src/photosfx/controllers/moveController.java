package photosfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

import photosfx.models.User;
import photosfx.models.Album;
import photosfx.models.Photo;


public class moveController {

    @FXML ListView<Album> albumListView;
    private SelectionModel<Album> albumSelectionModel;
    private User user;
    private Album currentAlbum;
    private Photo currentPhoto;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initializeData(User user, Album currentAlbum, Photo currentPhoto) {
        this.user = user;
        this.currentAlbum = currentAlbum;
        this.currentPhoto = currentPhoto;

        //populate with album names 
        albumListView.setItems(FXCollections.observableArrayList(user.getAlbumsExcept(currentAlbum)));
        albumSelectionModel = albumListView.getSelectionModel();
        //select album to move to
        albumSelectionModel.selectFirst();
    }

    @FXML
    private void handleMovePhoto(ActionEvent event) {
        Album targetAlbum = albumSelectionModel.getSelectedItem();
        if (targetAlbum == null || targetAlbum.equals(currentAlbum)) {
            //error
            return;
        }

        

    }


}
