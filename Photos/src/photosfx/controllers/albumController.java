package photosfx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListCell;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import photosfx.models.User;
import photosfx.models.Admin;
import photosfx.models.Album;
import photosfx.models.Photo;
import photosfx.models.Tags;

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

//image list in EACH alb

@FXML ListView<Photo> imgs; 

@FXML 
ListView<String> imgNamesListView;

private ObservableList<String> photoNames;
//list of tags for EACH img

@FXML ListView<String> tagsList;

private ObservableList<String> tagObservableList;

@FXML Text captionText, dateText, albumText; 

@FXML ImageView dispImg; 

// tag selection
private SelectionModel<String> selectedTag; 

//private lastSelectedImage;
private static String lastSelectedPhotoName;



@FXML
private void initialize() {
    loggedInUser = User.loadUser(username);
    photoNames = FXCollections.observableArrayList();
    tagObservableList = FXCollections.observableArrayList();
    album = loggedInUser.getAlbum(inputAlbumName);
    albumText.setText(album.getName());
    refreshPhotosList();
    imgNamesListView.setOnMouseClicked(event -> {
        String selectedPhotoName = imgNamesListView.getSelectionModel().getSelectedItem();
        lastSelectedPhotoName = selectedPhotoName;
        imgDISP(selectedPhotoName);
    });
    imgNamesListView.setOnKeyPressed(event -> {
        String selectedPhotoName = imgNamesListView.getSelectionModel().getSelectedItem();
        lastSelectedPhotoName = selectedPhotoName;
        if (selectedPhotoName != null) {
            imgDISP(selectedPhotoName);
        }
    });
}

private void refreshPhotosList(){
    ArrayList<String> photoArraylist = new ArrayList<>();
    for (Photo photo : album.getPhotos()) {
        photoArraylist.add(photo.getFilePath());
    }
    photoNames.clear();
    photoNames.addAll(photoArraylist);
    imgNamesListView.setItems(photoNames);
    Admin.saveUsers("Photos/data/users.ser");
}

public static void initData(Album album, String name) {
    inputAlbumName = album.getName();
    username = name;
}



//pop images into specific album

// private void refresh() {

//     imgs.setItems(FXCollections.observableArrayList(album.getPhotos()));
//     imgs.setCellFactory(param -> new ListCell<Photo>() {

//         private ImageView imgPrev = new ImageView();


//         @Override
//         public void updateItem(Photo photo, boolean empty) {
//             super.updateItem(photo, empty);

//             if (empty || photo == null) {
//                 setText(null);
//                 setGraphic(null);

//             } else {

//                 try {
//                     String photoPath = photo.getFilePath(); 
//                     imgPrev.setImage(new Image(new FileInputStream(photoPath)));
//                 } catch (Exception e) {
//                     e.printStackTrace(); // Consider more robust error handling
//                 }

//                 imgPrev.setFitWidth(100);

//                 //imgPrev.setPreserveRatio(true);

//                 setText("caption: " + photo.getCaption());

//                 setGraphic(imgPrev);

//             }
//         }
//     });
//     //selectedImage = imgs.getSelectionModel();


//     //selectedImage.selectedIndexProperty().addListener((obs, oldVal, newVal) -> imgDISP());

//     //if(subsetPhoto == null) selectedImage.selectFirst();


//     //else selectedImage.select(subsetPhoto);

// }

private void imgDISP(String selectedPhotoName ) {
    //current photo
    Photo selectedPhoto = null;
    for (Photo photo : album.getPhotos()) {
        if (photo.getFilePath().equals(selectedPhotoName)) {
            selectedPhoto = photo;
            break;
        }
    }
    
    // return cleared disp if no photo is selected
    if (selectedPhoto == null) {
        dispImg.setImage(null); 
        captionText.setText("caption: ");
        dateText.setText("date: "); 
        tagsList.setItems(null); //clear tags
        return;
    }
    //tagsList stuff
    if (tagsList != null) {
        tagObservableList = FXCollections.observableArrayList(selectedPhoto.getTagsAsList());
        tagsList.setItems(tagObservableList);
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
    captionText.setText("caption: " + selectedPhoto.getCaption());
    dateText.setText("date: " + selectedPhoto.getDateTime()); 

    //populate the list of tags for the selected photo
    tagsList.setItems(selectedPhoto.getTagsAsList());

    //set up tag selection 
    selectedTag = tagsList.getSelectionModel();
    selectedTag.selectFirst(); //default to first tag
}

public void AddPhotos() { 
    FileChooser pickIMG = new FileChooser();
    pickIMG.setTitle("Choose Photo");
        File selectedFile = pickIMG.showOpenDialog(new Stage());
        if (selectedFile != null) {
            String captionstr = "Default Caption";
            // ask for caption
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Caption Image");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter Caption:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                captionstr = result.get();
            }
            // Create a Photo object and add it to the album
            Photo photo = new Photo(selectedFile.getPath(), Photo.getLastModifiedDateTime(selectedFile), captionstr);
            album.addPhoto(photo);
            Admin.saveUsers("Photos/data/users.ser");
            refreshPhotosList();
            imgDISP(photo.getFilePath());
            System.out.println("Photo added to album: " + selectedFile.getName());
        }
        
       

}


public void delPhoto() { 
    String photoFilename = imgNamesListView.getSelectionModel().getSelectedItem();
    lastSelectedPhotoName = photoFilename;
    if (photoFilename != null) {
        for (Photo photo : album.getPhotos()) {
            if (photo.getFilePath().equals(photoFilename)) {
                album.removePhoto(photo);
            Admin.saveUsers("Photos/data/users.ser");
            refreshPhotosList();
            imgDISP(null); //resets display
            System.out.println("Photo deleted from album: " + photoFilename);
            break;
            }
        }
    } else {
        Admin.showAlert(Alert.AlertType.ERROR, "Error", "Please select a photo to delete.");
        System.out.println("Please select a photo to delete.");
    }
}

public void addTagView(final ActionEvent e) { 

}

public void delTag(final ActionEvent e) { 

}


public void renameCaption() { 
    String photoFilename = imgNamesListView.getSelectionModel().getSelectedItem();
    lastSelectedPhotoName = photoFilename;
    if (photoFilename != null) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rename Caption");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new caption");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(rename -> {
            if(rename == null || rename.isEmpty()) {
                Admin.showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid username.");
                System.out.println("Please enter a valid username.");
            } else {
                for (Photo photo : album.getPhotos()) {
                    if (photo.getFilePath().equals(photoFilename)) {
                        photo.setCaption(rename);
                        break;
                    }
                }
                Admin.saveUsers("Photos/data/users.ser");
                refreshPhotosList();
                //refresh the display for the caption
                
                imgDISP(lastSelectedPhotoName);
                System.out.println(photoFilename + " caption renamed to " + rename);
            }
        });
    } else {
        Admin.showAlert(Alert.AlertType.ERROR, "Error", "Please select a photo to edit the caption of.");
        System.out.println("Please select a photo to edit the caption of.");
    }
    
}

public void copyPhoto() { 
    String photoFilename = imgNamesListView.getSelectionModel().getSelectedItem();
    lastSelectedPhotoName = photoFilename;
    if (photoFilename != null) {
        List<Album> userAlbums = loggedInUser.getAlbums();
        List<String> albumNames = new ArrayList<>();
        for (Album album : userAlbums) {
            if (!album.getName().equals(inputAlbumName)) {
                albumNames.add(album.getName());
            }
        }


        ChoiceDialog<String> dialog = new ChoiceDialog<>(albumNames.get(0), albumNames); // Drop down list of albums to copy the photo to
        dialog.setTitle("Copy Photo");
        dialog.setHeaderText("Select the album to copy the photo to:");
        dialog.setContentText("Album:");

        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selectedAlbumName -> {
            Album selectedAlbum = loggedInUser.getAlbum(selectedAlbumName);
            if (selectedAlbum != null) {
                album.copyPhoto(photoFilename, selectedAlbum); // Call the copyPhoto method in Album class to copy the photo
                Admin.saveUsers("Photos/data/users.ser");
                refreshPhotosList();
                System.out.println("Photo copied to album: " + selectedAlbumName);
            } else {
                Admin.showAlert(Alert.AlertType.ERROR, "Error", "Selected album not found.");
                System.out.println("Selected album not found.");
            }
        });
    } else {
        Admin.showAlert(Alert.AlertType.ERROR, "Error", "Please select a photo to copy.");
        System.out.println("Please select a photo to copy.");
    }
}


public void movePhoto() { 
    String photoFilename = imgNamesListView.getSelectionModel().getSelectedItem();
    lastSelectedPhotoName = photoFilename;
    if (photoFilename != null) {
        List<Album> userAlbums = loggedInUser.getAlbums();
        List<String> albumNames = new ArrayList<>();
        for (Album album : userAlbums) {
            if (!album.getName().equals(inputAlbumName)) {
                albumNames.add(album.getName());
            }
        }


        ChoiceDialog<String> dialog = new ChoiceDialog<>(albumNames.get(0), albumNames); // Drop down list of albums to copy the photo to
        dialog.setTitle("Move Photo");
        dialog.setHeaderText("Select the album to move the photo to:");
        dialog.setContentText("Album:");

        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selectedAlbumName -> {
            Album selectedAlbum = loggedInUser.getAlbum(selectedAlbumName);
            if (selectedAlbum != null) {
                album.movePhoto(photoFilename, selectedAlbum); // Call the copyPhoto method in Album class to copy the photo
                Admin.saveUsers("Photos/data/users.ser");
                refreshPhotosList();
            } else {
                Admin.showAlert(Alert.AlertType.ERROR, "Error", "Selected album not found.");
                System.out.println("Selected album not found.");
            }
        });
    } else {
        Admin.showAlert(Alert.AlertType.ERROR, "Error", "Please select a photo to copy.");
        System.out.println("Please select a photo to copy.");
    }
}


public void loadSlideShow(final ActionEvent e) throws IOException {
    // Make sure we have photos in the album to show
    if (album.getPhotos().isEmpty()) {
        Admin.showAlert(Alert.AlertType.ERROR, "Album empty", "Please add a photo to this album, then try again.");
        return;
    }

    // Load the slideshow view
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/photosfx/view/slideshow.fxml")); // Adjust the path as needed
    Parent root = loader.load();

    // Get the SlideshowController and set the user and album
    slideshowControl slideshowControl = loader.getController();
    slideshowControl.setLoadState(username, album.getName());

    // Show the slideshow stage
    Stage stage = new Stage();
    slideshowControl.setStage(stage);
    stage.setScene(new Scene(root));
    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(((Node)e.getSource()).getScene().getWindow());
    stage.show();
}



    
}
