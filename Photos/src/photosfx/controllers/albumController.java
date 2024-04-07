package photosfx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

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
            // Configure image view properties
             imageView.setFitWidth(100);
             imageView.setFitHeight(100);
    
             // Configure caption text properties
             captionText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
             captionText.setWrappingWidth(100); // Adjust as needed
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
        imgDISP(selectedPhoto.getFilePath());
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
            //Admin.saveUsers("Photos/data/users.ser");
            refreshPhotosList();
            imgDISP(photo.getFilePath());
            System.out.println("Photo added to album: " + selectedFile.getName());
        } else {
            System.out.println("No file selected.");
        }
        
       

}


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
        System.out.println("Photo deleted from album: " + selectedPhoto.getFilePath());
    }
}

public void addTagView(final ActionEvent e) { 

}

public void delTag(final ActionEvent e) { 

}


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
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(rename -> {
            if(rename == null || rename.isEmpty()) {
                Admin.showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid username.");
                System.out.println("Please enter a valid username.");
            } else {
                selectedPhoto.setCaption(rename);
                Admin.saveUsers("Photos/data/users.ser");
                refreshPhotosList();
                //refresh the display for the caption
                
                imgDISP(selectedPhoto.getFilePath());
                System.out.println(selectedPhoto.getFilePath() + " caption renamed to " + rename);
                
            }
            
        });
    }    
}

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
            System.out.println("No albums to copy the photo to.");
            return;
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(albumNames.get(0), albumNames); // Drop down list of albums to copy the photo to
        dialog.setTitle("Copy Photo");
        dialog.setHeaderText("Select the album to copy the photo to:");
        dialog.setContentText("Album:");

        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selectedAlbumName -> {
            Album selectedAlbum = loggedInUser.getAlbum(selectedAlbumName);
            if (selectedAlbum != null) {
                album.copyPhoto(selectedPhoto.getFilePath(), selectedAlbum); // Call the copyPhoto method in Album class to copy the photo
                Admin.saveUsers("Photos/data/users.ser");
                refreshPhotosList();
                System.out.println("Photo copied to album: " + selectedAlbumName);
            } else {
                Admin.showAlert(Alert.AlertType.ERROR, "Error", "Selected album not found.");
                System.out.println("Selected album not found.");
            }
        });
    }   
}


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
            System.out.println("No albums to copy the photo to.");
            return;
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(albumNames.get(0), albumNames); // Drop down list of albums to copy the photo to
        dialog.setTitle("Move Photo");
        dialog.setHeaderText("Select the album to move the photo to:");
        dialog.setContentText("Album:");

        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selectedAlbumName -> {
            Album selectedAlbum = loggedInUser.getAlbum(selectedAlbumName);
            if (selectedAlbum != null) {
                album.movePhoto(selectedPhoto.getFilePath(), selectedAlbum); // Call the copyPhoto method in Album class to copy the photo
                Admin.saveUsers("Photos/data/users.ser");
                refreshPhotosList();
            } else {
                Admin.showAlert(Alert.AlertType.ERROR, "Error", "Selected album not found.");
                System.out.println("Selected album not found.");
            }
        });
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
            stage.setTitle("Modify Tags");
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
    }
}


public static void setStage(Stage s) {
    stage = s;
}
public void quit(final ActionEvent e) throws IOException {
	stage.close();
}

    
//Below is the code for the search functionality

private void updateSearchButtonState() {
    LocalDateTime startDate = startDatePicker.getValue() != null ? startDatePicker.getValue().atStartOfDay() : null;
    LocalDateTime endDate = endDatePicker.getValue() != null ? endDatePicker.getValue().atStartOfDay() : null;
    String searchQuery = searchQueryField.getText().trim();

    // Enable search button if either date pickers have values or search query is not empty
    boolean enableSearch = (startDate != null || endDate != null) || !searchQuery.isEmpty();
    searchButton.setDisable(!enableSearch);
}
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
            filteredPhotos.addAll(searchByDateRange(startDate, endDate));
            filteredPhotos.retainAll(searchByTag(searchQuery));
        }
    } 
    else if (startDate != null && endDate == null && !searchQuery.isEmpty()) {
        filteredPhotos.addAll(searchByAfterStartDate(startDate));
        filteredPhotos.retainAll(searchByTag(searchQuery));
    } 
    else if (startDate == null && endDate != null && !searchQuery.isEmpty()) {
        filteredPhotos.addAll(searchByBeforeEndDate(endDate));
        filteredPhotos.retainAll(searchByTag(searchQuery));
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

public void clearSearch() {
    startDatePicker.setValue(null);
    endDatePicker.setValue(null);
    searchQueryField.clear();
    refreshPhotosList();
}

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






}
