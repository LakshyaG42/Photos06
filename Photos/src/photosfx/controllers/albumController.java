package photosfx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import photosfx.models.User;
import photosfx.models.Admin;
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

//image list in EACH alb

@FXML ListView<Photo> imgs; 

@FXML 
ListView<String> imgNamesListView;

private ObservableList<String> photoNames;
//list of tags for EACH img

@FXML ListView<Photo> photoListView = new ListView<>();

private ObservableList<Photo> photoObservableList;


@FXML ListView<String> tagsList;

private ObservableList<String> tagObservableList;

@FXML Text captionText, dateText, albumText; 

@FXML ImageView dispImg; 

private static Stage stage;

// tag selection
private SelectionModel<String> selectedTag; 

//private lastSelectedImage;
//private static String lastSelectedPhotoName;


//Search functionality
@FXML private TextField searchQueryField;
@FXML private DatePicker startDatePicker;
@FXML private DatePicker endDatePicker;
@FXML private Button searchButton;


/**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It sets up the album view, loading
     * photos and setting up listeners for UI components.
     */

@FXML
private void initialize() {
    loggedInUser = User.loadUser(username);
    photoNames = FXCollections.observableArrayList();
    tagObservableList = FXCollections.observableArrayList();
    album = loggedInUser.getAlbum(inputAlbumName);
    albumText.setText(album.getName());
    photoObservableList = FXCollections.observableArrayList();
    refreshPhotosList();

    startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> updateSearchButtonState());
    endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> updateSearchButtonState());
    searchQueryField.textProperty().addListener((observable, oldValue, newValue) -> updateSearchButtonState());
    searchButton.setDisable(true);
    photoListView.setCellFactory(param -> new ListCell<Photo>() {
         private final ImageView imageView = new ImageView();
         private final Text captionText = new Text();
    
         {
            //images
             imageView.setFitWidth(100);
             imageView.setFitHeight(100);
    
             //caption
             captionText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
             captionText.setWrappingWidth(100); 
             captionText.setFill(Color.WHITE);
         }
    
         @Override
         protected void updateItem(Photo photo, boolean empty) {
             super.updateItem(photo, empty);
    
             if (empty || photo == null) {
                 setText(null);
                 setGraphic(null);
             } else {
                 // Set thumbnail image and caption
                 if(username.equals("stock")) {
                    imageView.setImage(new Image("file:///" + new File(photo.getFilePath()).getAbsolutePath().replace("\\", "/")));
                 } else {
                     imageView.setImage(new Image("file:///" + photo.getFilePath()));
                 }
                 captionText.setText(photo.getCaption());
    
                 // Set graphic for the cell
                 setGraphic(new VBox(imageView, captionText));
             }
         }
     });


    photoListView.setOnMouseClicked(event -> {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        //lastSelectedPhotoName = selectedPhoto.getFilePath();
        if(selectedPhoto != null) {
            imgDISP(selectedPhoto.getFilePath());
        }
    });
    photoListView.setOnKeyPressed(event -> {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        //lastSelectedPhotoName = selectedPhoto.getFilePath();
        if (selectedPhoto.getFilePath() != null) {
            imgDISP(selectedPhoto.getFilePath());
        }
    });
    // imgNamesListView.setOnMouseClicked(event -> {
    //     String selectedPhotoName = imgNamesListView.getSelectionModel().getSelectedItem();
    //      lastSelectedPhotoName = selectedPhotoName;
    //      imgDISP(selectedPhotoName);
    //  });
    //  imgNamesListView.setOnKeyPressed(event -> {
    //      String selectedPhotoName = imgNamesListView.getSelectionModel().getSelectedItem();
    //      lastSelectedPhotoName = selectedPhotoName;
    //      if (selectedPhotoName != null) {
    //          imgDISP(selectedPhotoName);
    //     }
    //  });
}

/**
     * Refreshes the list of photos displayed in the album. This is typically called
     * after photos have been added or removed, or tags have been changed.
     */

private void refreshPhotosList(){
    // ArrayList<String> photoArraylist = new ArrayList<>();
    // for (Photo photo : album.getPhotos()) {
    //    photoArraylist.add(photo.getFilePath());
    // }
    // photoNames.clear();
    // photoNames.addAll(photoArraylist);
    // imgNamesListView.setItems(photoNames);
    photoObservableList.clear();
    photoObservableList.addAll(album.getPhotos());
    photoListView.setItems(photoObservableList);

    Admin.saveUsers("Photos/data/users.ser");
}


/**
     * Initializes the controller with data from another scene, typically the user controller.
     * @param album The album object to be managed by this controller.
     * @param name The username of the logged-in user.
     */

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


/**
 * Displays the selected photo in the album view. This method updates the photo display area,
 * including the image itself, the caption, and the date the photo was taken. It also updates
 * the list of tags associated with the photo.
 * 
 * If the selected photo is null (indicating no photo is currently selected), this method
 * clears the photo display area.
 *
 * @param selectedPhotoName The file path of the photo to display. If null or not found,
 *                          the display area is cleared.
 */
private void imgDISP(String selectedPhotoName) {
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

/**
     * Adds a new photo to the album. Opens a FileChooser dialog for the user to select a photo,
     * then adds the selected photo to the album and refreshes the photo list.
     */
public void AddPhotos() { 
    FileChooser pickIMG = new FileChooser();
    pickIMG.setTitle("Choose Photo");
        File selectedFile = pickIMG.showOpenDialog(new Stage());
        if (selectedFile != null) {
            String captionstr = "Default Caption";
            // Create a Photo object and add it to the album
            Photo existingPhoto = null;
            Album existingAlbum = null;
            for(Album album : loggedInUser.getAlbums()){
                for (Photo photo : album.getPhotos()) {
                    if (photo.getFilePath().equals(selectedFile.getPath())) {
                        existingAlbum = album;
                        existingPhoto = photo;
                        break;
                    }
                }
            }
            if (existingPhoto != null) {
                album.addPhoto(existingPhoto);
                Admin.showAlert(AlertType.INFORMATION, "Photo Already Exists In Another Album (Copying)", "The selected photo already exists in: " + existingAlbum.getName());

                refreshPhotosList();
                imgDISP(existingPhoto.getFilePath());
            } else {
                // ask for caption
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Caption Image");
                dialog.setHeaderText(null);
                dialog.setContentText("Enter Caption:");
                Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
                dialogStage.getIcons().add(
                    new Image("file:///" + new File("Photos/data/userPhotos/LogoMain.png").getAbsolutePath().replace("\\", "/")));   
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    captionstr = result.get();
                }
                Photo photo = new Photo(selectedFile.getPath(), Photo.getLastModifiedDateTime(selectedFile), captionstr);
                album.addPhoto(photo);
                //Admin.saveUsers("Photos/data/users.ser");
                refreshPhotosList();
                imgDISP(photo.getFilePath());
            }
        } 
        
       

}

/**
     * Deletes the currently selected photo from the album. Prompts the user for confirmation
     * before deletion.
     */
public void delPhoto() { 
    //String photoFilename = imgNamesListView.getSelectionModel().getSelectedItem();
    Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
    if (selectedPhoto == null) {
        Admin.showAlert(Alert.AlertType.ERROR, "Error", "Please select a photo to delete.");
    } else {
        //lastSelectedPhotoName = selectedPhoto.getFilePath();
        album.removePhoto(selectedPhoto);
        //Admin.saveUsers("Photos/data/users.ser");
        refreshPhotosList();
        imgDISP(null); //resets display
        
    }
}

//public void addTagView(final ActionEvent e) { 

//}

//public void delTag(final ActionEvent e) { 

//}


/**
 * Prompts the user to enter a new caption for the selected photo. If the user provides a new caption, 
 * the photo's caption is updated. This method also saves the updated users' data to the file and refreshes 
 * the photo list to reflect the changes.
 */
public void renameCaption() { 
    ///String photoFilename = imgNamesListView.getSelectionModel().getSelectedItem();
    Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
    if(selectedPhoto == null) {
        Admin.showAlert(Alert.AlertType.ERROR, "Error", "Please select a photo to rename the caption.");
    } else {
        //lastSelectedPhotoName = selectedPhoto.getFilePath();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rename Caption");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new caption");
        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(
            new Image("file:///" + new File("Photos/data/userPhotos/LogoMain.png").getAbsolutePath().replace("\\", "/")));

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(rename -> {
            if(rename == null || rename.isEmpty()) {
                Admin.showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid username.");
                
            } else {
                selectedPhoto.setCaption(rename);
                Admin.saveUsers("Photos/data/users.ser");
                refreshPhotosList();
                //refresh the display for the caption
                
                imgDISP(selectedPhoto.getFilePath());
                
                
            }
            
        });
    }    
}

/**
 * Allows the user to copy the selected photo to another album. Presents a choice dialog to select the destination album.
 * If the selected album already contains the photo, an error alert is displayed. Otherwise, the photo is copied, users'
 * data is saved, and the photo list is refreshed.
 */
public void copyPhoto() { 
    Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
    if (selectedPhoto == null) {
        Admin.showAlert(Alert.AlertType.ERROR, "Error", "Please select a photo to copy.");
    } else {
        //lastSelectedPhotoName = selectedPhoto.getFilePath();
        List<Album> userAlbums = loggedInUser.getAlbums();
        List<String> albumNames = new ArrayList<>();
        for (Album album : userAlbums) {
            if (!album.getName().equals(inputAlbumName)) {
                albumNames.add(album.getName());
            }
        }
        if (albumNames.isEmpty()) {
            Admin.showAlert(Alert.AlertType.ERROR, "Error", "No albums to copy the photo to.");
            
            return;
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(albumNames.get(0), albumNames); // Drop down list of albums to copy the photo to
        dialog.setTitle("Copy Photo");
        dialog.setHeaderText("Select the album to copy the photo to:");
        dialog.setContentText("Album:");
        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(
            new Image("file:///" + new File("Photos/data/userPhotos/LogoMain.png").getAbsolutePath().replace("\\", "/")));
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selectedAlbumName -> {
            Album selectedAlbum = loggedInUser.getAlbum(selectedAlbumName);
            if (selectedAlbum != null) {
                album.copyPhoto(selectedPhoto.getFilePath(), selectedAlbum); // Call the copyPhoto method in Album class to copy the photo
                Admin.saveUsers("Photos/data/users.ser");
                refreshPhotosList();
                
            } else {
                Admin.showAlert(Alert.AlertType.ERROR, "Error", "Selected album not found.");
                
            }
        });
    }   
}

/**
 * Moves the selected photo to another album chosen by the user. Similar to copyPhoto, but the photo is removed from the 
 * current album after being added to the destination album. Updates the albums accordingly, saves the changes, and refreshes
 * the photo list.
 */
public void movePhoto() { 
    Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
    if (selectedPhoto == null) {
        Admin.showAlert(Alert.AlertType.ERROR, "Error", "Please select a photo to move.");
    } else {
        //lastSelectedPhotoName = selectedPhoto.getFilePath();
        List<Album> userAlbums = loggedInUser.getAlbums();
        List<String> albumNames = new ArrayList<>();
        for (Album album : userAlbums) {
            if (!album.getName().equals(inputAlbumName)) {
                albumNames.add(album.getName());
            }
        }
        if (albumNames.isEmpty()) {
            Admin.showAlert(Alert.AlertType.ERROR, "Error", "No albums to copy the photo to.");
            
            return;
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(albumNames.get(0), albumNames); // Drop down list of albums to copy the photo to
        dialog.setTitle("Move Photo");
        dialog.setHeaderText("Select the album to move the photo to:");
        dialog.setContentText("Album:");
        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(
            new Image("file:///" + new File("Photos/data/userPhotos/LogoMain.png").getAbsolutePath().replace("\\", "/")));
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selectedAlbumName -> {
            Album selectedAlbum = loggedInUser.getAlbum(selectedAlbumName);
            if (selectedAlbum != null) {
                album.movePhoto(selectedPhoto.getFilePath(), selectedAlbum); // Call the copyPhoto method in Album class to copy the photo
                Admin.saveUsers("Photos/data/users.ser");
                refreshPhotosList();
            } else {
                Admin.showAlert(Alert.AlertType.ERROR, "Error", "Selected album not found.");
                
            }
        });
    }
}

/**
 * Launches a slideshow of the current album's photos. Checks if the album contains any photos and, if so, loads and displays 
 * them in a slideshow. Utilizes the slideshowControl to manage the slideshow's functionality.
 *
 * @param e The ActionEvent triggering this method.
 * @throws IOException if there's an issue loading the slideshow view.
 */
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
    stage.getIcons().add(new Image("file:///" + new File("Photos/data/userPhotos/LogoMain.png").getAbsolutePath().replace("\\", "/")));
    stage.setResizable(false);
    stage.setTitle("Slideshow");
    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(((Node)e.getSource()).getScene().getWindow());
    stage.show();
}

/**
 * Opens the "Modify Tags" view for the selected photo. If a photo is selected, this method loads the modify tags view,
 * allowing the user to add, remove, or change tags associated with the photo. After the tags view is closed, it refreshes
 * the album view to reflect any changes made to the tags.
 */
@FXML
public void loadModifyTags() {
    Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
        try {
            tagController.setData(selectedPhoto.getFilePath(), username, album.getName());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/photosfx/view/modifyTags.fxml"));
            Parent root = loader.load();
            tagController controller = loader.getController();
            Stage stage = new Stage();
            controller.setStage(stage);
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("file:///" + new File("Photos/data/userPhotos/LogoMain.png").getAbsolutePath().replace("\\", "/")));
            stage.setTitle("Modify Tags");
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tagsList.getScene().getWindow());
            stage.show();

            stage.setOnHidden(e -> refreshAlbumView());

        } catch (IOException e) {
            Admin.showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Modify Tags view");
            e.printStackTrace();
        }
    } else {
        Admin.showAlert(Alert.AlertType.ERROR, "Error", "Please select a photo to modify tags.");
    }
}

/**
 * Refreshes the current view of the album. This is typically called after modifications to the album's photos
 * or their properties (such as tags) to ensure the user interface reflects the latest data.
 */
private void refreshAlbumView() {
    //reload user
    loggedInUser = User.loadUser(username);
    album = loggedInUser.getAlbum(inputAlbumName);
    
    //refresh obs list from list view
    refreshPhotosList();
    
    //refresh disp
    Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
        imgDISP(selectedPhoto.getFilePath());
    } else {
        imgDISP(null);
    }
}

/**
 * Sets the stage for the controller. This is used to store a reference to the stage (window) 
 * in which this controller's scene is displayed.
 *
 * @param s The stage (window) in which this controller's scene is displayed.
 */
public static void setStage(Stage s) {
    stage = s;
}

/**
 * Handles the action to close the current stage (window). This is typically bound to a "Quit" or "Close" button
 * and is used to exit the current view.
 *
 * @param e The event that triggered this action.
 * @throws IOException if an I/O error occurs.
 */
public void quit(final ActionEvent e) throws IOException {
	stage.close();
}

    
//Below is the code for the search functionality

/**
 * Updates the state (enabled or disabled) of the search button based on the current input in the search fields.
 * The search button is enabled if there is any input in the search query field or any date selected in the date pickers.
 */
private void updateSearchButtonState() {
    LocalDateTime startDate = startDatePicker.getValue() != null ? startDatePicker.getValue().atStartOfDay() : null;
    LocalDateTime endDate = endDatePicker.getValue() != null ? endDatePicker.getValue().atStartOfDay() : null;
    String searchQuery = searchQueryField.getText().trim();

    // Enable search button if either date pickers have values or search query is not empty
    boolean enableSearch = (startDate != null || endDate != null) || !searchQuery.isEmpty();
    searchButton.setDisable(!enableSearch);
}

/**
 * Executes the search based on the criteria entered by the user. This can include a search query and/or date range.
 * The method filters photos in the current album according to these criteria and updates the photo list view
 * to only display photos that match the search.
 */
public void search() {
    LocalDateTime startDate = startDatePicker.getValue() != null ? startDatePicker.getValue().atStartOfDay() : null;
    LocalDateTime endDate = endDatePicker.getValue() != null ? endDatePicker.getValue().atStartOfDay() : null;
    String searchQuery = searchQueryField.getText().trim();
    ObservableList<Photo> filteredPhotos = FXCollections.observableArrayList();

    if (startDate != null && endDate != null && searchQuery.isEmpty()) {
        if (startDate.isAfter(endDate)) {
            Admin.showAlert(Alert.AlertType.ERROR, "Error", "Start date cannot be after end date.");
            return;
        } else {
            filteredPhotos.addAll(searchByDateRange(startDate, endDate));
        }
    } 
    else if (startDate != null && endDate == null && searchQuery.isEmpty()) {
        filteredPhotos.addAll(searchByAfterStartDate(startDate));
    } 
    else if (startDate == null && endDate != null && searchQuery.isEmpty()) {
        filteredPhotos.addAll(searchByBeforeEndDate(endDate));
    } 
    else if (startDate == null && endDate == null && !searchQuery.isEmpty()) {
        filteredPhotos.addAll(searchByTag(searchQuery));
    } 
    else if (startDate != null && endDate != null && !searchQuery.isEmpty()) {
        if (startDate.isAfter(endDate)) {
            Admin.showAlert(Alert.AlertType.ERROR, "Error", "Start date cannot be after end date.");
            return;
        } else {
            Optional<ButtonType> result = Admin.showAlert(Alert.AlertType.CONFIRMATION, "Joint Search Requested", "Searching by date range and tag! Please reset and only input one if a disjoint search is desired.");
            if(result.get() == ButtonType.OK) {
                filteredPhotos.addAll(searchByDateRange(startDate, endDate));
                filteredPhotos.retainAll(searchByTag(searchQuery));
            } else {
                return;
            }
        }
    } 
    else if (startDate != null && endDate == null && !searchQuery.isEmpty()) {
        Optional<ButtonType> result = Admin.showAlert(Alert.AlertType.CONFIRMATION, "Joint Search Requested", "Searching by date range and tag! Please reset and only input one if a disjoint search is desired.");
            if(result.get() == ButtonType.OK) {
                filteredPhotos.addAll(searchByAfterStartDate(startDate));
                filteredPhotos.retainAll(searchByTag(searchQuery));
            } else {
                return;
            }
    } 
    else if (startDate == null && endDate != null && !searchQuery.isEmpty()) {
        Optional<ButtonType> result = Admin.showAlert(Alert.AlertType.CONFIRMATION, "Joint Search Requested", "Searching by date range and tag! Please reset and only input one if a disjoint search is desired.");
            if(result.get() == ButtonType.OK) {
                filteredPhotos.addAll(searchByBeforeEndDate(endDate));
                filteredPhotos.retainAll(searchByTag(searchQuery));
            } else {
                return;
            }
    } 
    else if (startDate == null && endDate == null && !searchQuery.isEmpty()) {
        filteredPhotos.addAll(searchByTag(searchQuery));
    } 
    else {
        Admin.showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid search query.");
        return;
    }
    // Remove duplicate values by using and iterator:
    Set<String> filenames = new HashSet<>();
    List<Photo> photosToKeep = new ArrayList<>();
    for (Photo photo : filteredPhotos) {
        if (filenames.add(photo.getFilePath())) {
            photosToKeep.add(photo);
        }
    }
    filteredPhotos.clear();
    filteredPhotos.addAll(photosToKeep);
    photoObservableList.clear();
    photoObservableList.addAll(filteredPhotos);
    photoListView.setItems(photoObservableList);
}

/**
 * Clears all search criteria fields and resets the photo list view to show all photos in the album.
 */
public void clearSearch() {
    startDatePicker.setValue(null);
    endDatePicker.setValue(null);
    searchQueryField.clear();
    refreshPhotosList();
}

/**
 * Filters and returns photos within a specified date range.
 *
 * @param startDate The start date of the range, inclusive.
 * @param endDate The end date of the range, inclusive.
 * @return An ObservableList of Photo objects that fall within the specified date range.
 */
public ObservableList<Photo> searchByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
    ObservableList<Photo> filteredPhotos = FXCollections.observableArrayList();
    for (Photo photo : album.getPhotos()) {
        LocalDateTime photoDateTime = photo.getDateTime();
        if (photoDateTime.isAfter(startDate) && photoDateTime.isBefore(endDate)) {
            filteredPhotos.add(photo);
        }
    }
    return filteredPhotos;
}

/**
 * Filters and returns photos taken after a specified start date.
 *
 * @param startDate The start date from which to filter photos.
 * @return An ObservableList of Photo objects taken after the specified start date.
 */
public ObservableList<Photo> searchByAfterStartDate(LocalDateTime startDate) {
    ObservableList<Photo> filteredPhotos = FXCollections.observableArrayList();
    for (Photo photo : album.getPhotos()) {
        LocalDateTime photoDateTime = photo.getDateTime();
        if (photoDateTime.isAfter(startDate)) {
            filteredPhotos.add(photo);
        }
    }
    return filteredPhotos;
}

/**
 * Filters and returns photos taken before a specified end date.
 *
 * @param endDate The end date to which to filter photos.
 * @return An ObservableList of Photo objects taken before the specified end date.
 */
public ObservableList<Photo> searchByBeforeEndDate(LocalDateTime endDate) {
    ObservableList<Photo> filteredPhotos = FXCollections.observableArrayList();
    for (Photo photo : album.getPhotos()) {
        LocalDateTime photoDateTime = photo.getDateTime();
        if (photoDateTime.isBefore(endDate)) {
            filteredPhotos.add(photo);
        }
    }
    return filteredPhotos;
}

/**
 * Filters and returns photos based on tag criteria. Supports searching by single tags, conjunctions (AND),
 * or disjunctions (OR) of tag key-value pairs.
 *
 * @param input The search input string containing tag key-value pairs, and optionally "AND"/"OR" for complex queries.
 * @return An ObservableList of Photo objects that match the tag criteria.
 */
public ObservableList<Photo> searchByTag(String input) {
    ObservableList<Photo> filteredPhotos = FXCollections.observableArrayList();
    if (input.contains("AND")) {
        // Handle conjunction (AND) case
        String[] tagParts = input.split("\\s+AND\\s+");
        if (tagParts.length == 2) {
            String[] firstTag = tagParts[0].split(":");
            String[] secondTag = tagParts[1].split(":");
            if (firstTag.length == 2 && secondTag.length == 2) {
                String key1 = firstTag[0].trim();
                String value1 = firstTag[1].trim();
                String key2 = secondTag[0].trim();
                String value2 = secondTag[1].trim();

                for (Photo photo : album.getPhotos()) {
                    if (photo.getTags().containsKey(key1) && photo.getTags().containsKey(key2)) {
                        if (photo.getTags().get(key1).contains(value1) && photo.getTags().get(key2).contains(value2)) {
                            filteredPhotos.add(photo);
                        }
                    }
                }
            }
        }
    } else if (input.contains("OR")) {
        // Handle disjunction (OR) case
        String[] tagParts = input.split("\\s+OR\\s+");
        for (String tagPart : tagParts) {
            String[] tag = tagPart.split(":");
            if (tag.length == 2) {
                String key = tag[0].trim();
                String value = tag[1].trim();
                for (Photo photo : album.getPhotos()) {
                    if (photo.getTags().containsKey(key) && photo.getTags().get(key).contains(value)) {
                        filteredPhotos.add(photo);
                    }
                }
            }
        }
    } else {
        // Handle single tag-value pair case
        String[] tag = input.split(":");
        if (tag.length == 2) {
            String key = tag[0].trim();
            String value = tag[1].trim();
            for (Photo photo : album.getPhotos()) {
                if (photo.getTags().containsKey(key) && photo.getTags().get(key).contains(value)) {
                    filteredPhotos.add(photo);
                }
            }
        }
    }
    return filteredPhotos;
}

/**
 * Prompts the user for an album name and creates a new album with the photos currently displayed in the view.
 * If the user provides a valid album name, a new album is created with the displayed photos, and the user is notified of success.
 * If the user cancels the dialog or enters an invalid name, they are alerted to the error.
 */
public void createAlbumFromDisplayedPhotos() {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Create Album from Displayed Photos");
    dialog.setHeaderText(null);
    dialog.setContentText("Enter Album Name:");
    Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(
            new Image("file:///" + new File("Photos/data/userPhotos/LogoMain.png").getAbsolutePath().replace("\\", "/")));

    Optional<String> result = dialog.showAndWait();
    if(result.isPresent()) {
        String albumName = result.get().trim();
        if (!albumName.isEmpty()) {
            Album newAlbum = new Album(albumName);
            for (Photo photo : photoListView.getItems()) {
                newAlbum.addPhoto(photo);
            }
            for (Album album : loggedInUser.getAlbums()) {
                if (album.getName().equals(albumName)) {
                    Admin.showAlert(Alert.AlertType.ERROR, "Error", "An album with that name already exists.");
                    return;
                }
            }
            loggedInUser.addAlbum(newAlbum);
            Admin.saveUsers("Photos/data/users.ser");
            Admin.showAlert(Alert.AlertType.INFORMATION, "Success", "Album created successfully.");
        } else {
            Admin.showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid album name.");
        }
    }
}




}
