package photosfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.ListCell;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import photosfx.models.User;
import photosfx.models.Album;
import photosfx.models.Photo;

/**
 * Album Controls
 * @author Dhruv Shidhaye
 * @author Lakshya Gour
 */

public class albumController {

//data from userController:
private static String inputAlbumName;
private static String username;
private User loggedInUser;
private Album album;

//photo for selection
private Photo subsetPhoto; 

//image list in EACH alb

@FXML ListView<Photo> imgs; 

//list of tags for EACH img

@FXML ListView<String> tags; 

//image selection

private SelectionModel<Photo> selectedImage; 

@FXML Text caption, date, albumName; 

@FXML ImageView dispImg; 

// tag selection
private SelectionModel<String> selectedTag; 

@FXML
private void initialize() {
    loggedInUser = User.loadUser(username);
    album = loggedInUser.getAlbum(inputAlbumName);
    //refreshPhotosList();
}

public static void initData(Album album, String name) {
    inputAlbumName = album.getName();
    username = name;
}

public void setAlbum(Album album) {
    this.album = album;
    albumName.setText(album.getName());
    refresh();
}


//pop images into specific album

private void refresh() {

    imgs.setItems(FXCollections.observableArrayList(album.getPhotos()));
    imgs.setCellFactory(param -> new ListCell<Photo>() {

        private ImageView imgPrev = new ImageView();


        @Override
        public void updateItem(Photo photo, boolean empty) {
            super.updateItem(photo, empty);

            if (empty || photo == null) {
                setText(null);
                setGraphic(null);

            } else {

                try {
                    String photoPath = photo.getFilePath(); 
                    imgPrev.setImage(new Image(new FileInputStream(photoPath)));
                } catch (Exception e) {
                    e.printStackTrace(); // Consider more robust error handling
                }

                imgPrev.setFitWidth(100);

                //imgPrev.setPreserveRatio(true);

                setText("caption: " + photo.getCaption());

                setGraphic(imgPrev);

            }
        }
    });
    selectedImage = imgs.getSelectionModel();


    selectedImage.selectedIndexProperty().addListener((obs, oldVal, newVal) -> imgDISP());

    if(subsetPhoto == null) selectedImage.selectFirst();


    else selectedImage.select(subsetPhoto);

}

private void imgDISP() {
    //current photo
    final Photo selectedPhoto = selectedImage.getSelectedItem();

    // return cleared disp if no photo is selected
    if (selectedPhoto == null) {
        dispImg.setImage(null); 
        caption.setText("caption: ");
        date.setText("date: "); 
        tags.setItems(null); //clear tags
        return;
    }

    //display selected photo img
    try {
        dispImg.setImage(new Image(new FileInputStream(selectedPhoto.getFilePath())));
    } catch (IOException e) {
        e.printStackTrace(); 
    }


    dispImg.setFitWidth(350);
    dispImg.setPreserveRatio(true);

    //display cap and date
    caption.setText("caption: " + selectedPhoto.getCaption());
    date.setText("date: " + selectedPhoto.getDateTime()); 

    //populate the list of tags for the selected photo
    tags.setItems(selectedPhoto.getTagsAsList());

    //set up tag selection 
    selectedTag = tags.getSelectionModel();
    selectedTag.selectFirst(); //defualt to first tag
}

public void loadAddPhotos(final ActionEvent e) { 

    FileChooser pickImgFile = new FileChooser(); 

}


public void delPhoto(final ActionEvent e) { 

}

public void addTagView(final ActionEvent e) { 

}

public void delTag(final ActionEvent e) { 

}

public void loadUserPage(final ActionEvent e) { 

}

public void renameCaptionView(final ActionEvent e) { 

}

public void copyPhotoView(final ActionEvent e) { 

}

public void movePhotoView(final ActionEvent e) { 

}

public void loadSlideShow(final ActionEvent e) { 

}



    
}
