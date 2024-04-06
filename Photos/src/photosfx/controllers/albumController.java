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


//user
private User user; 

//album
private Album album; 

//photo for selection
private Photo subsetPhoto; 

//image list in EACH alb

@FXML ListView<Photo> imgs; 

//list of tags for EACH img

@FXML ListView<List<String>> tags; 

//image selection

private SelectionModel<Photo> selectedImage; 

@FXML Text caption, date, albumName; 

@FXML ImageView dispImg; 

// tag selection
private SelectionModel<List<String>> selectedTag; 


public void albumContents(String username, String albumName, Photo p) {

 user = User.loadUser(username); 


}
    
}
