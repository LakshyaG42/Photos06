package photosfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.FileInputStream;
import java.io.IOException;

import photosfx.models.User;
import photosfx.models.Album;
import photosfx.models.Photo;

public class slideshowControl {

    @FXML Button nextButton; 
    @FXML Button prevButton; 

    @FXML ImageView imageDisplay; 
    
    @FXML private Stage stage;
    
    private ObservableList<Photo> album;

    private int index;

    private Album aSelect;

    private User user;

    /**
     * Updates the enabled/disabled state of navigation buttons based on the current photo index.
     */
    private void onSelect(){

		prevButton.setDisable(index == 0);
		nextButton.setDisable(index == album.size()-1);

	}

    /**
     * Sets the album to be displayed in the slideshow based on the user's selection.
     * Loads photos into an observable list and displays the first photo if available.
     * 
     * @param username The username of the currently logged-in user.
     * @param albumname The name of the album selected for the slideshow.
     */
    public void setLoadState(String username, String albumname) {
    try {
        this.user = User.loadUser(username);
    } catch (Exception e) {
        e.printStackTrace();
    }

    this.aSelect = user.getAlbum(albumname);
    
    // Convert the List to ObservableList using FXCollections
    album = FXCollections.observableArrayList(aSelect.getPhotos());

    if (!album.isEmpty()) {
        // Display the first photo if the album is not empty
        index = 0; 
        displayPhoto(index);
    }
    
    onSelect();
}

/**
     * Displays the photo at the specified index within the album.
     * 
     * @param index The index of the photo to display.
     */
private void displayPhoto(int index) {
    try {
        Image img = new Image(new FileInputStream(album.get(index).getFilePath()));
        imageDisplay.setImage(img);
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}

/**
     * Navigates to the previous photo in the album, if available.
     * 
     * @param e The action event triggering the navigation.
     */
public void previousImage(final ActionEvent e) {
    if (index > 0) {
        index--; 
        displayPhoto(index);
        onSelect();
    }
}

/**
     * Navigates to the next photo in the album, if available.
     * 
     * @param e The action event triggering the navigation.
     */
public void nextImage(final ActionEvent e) {
    if (index < album.size() - 1) {
        index++; 
        displayPhoto(index);
        onSelect();
    }
}

    /* 
    public void setLoadState(String username,String albumname){

		try {

			this.user = User.loadUser(username);

		}catch(Exception e) {}

		this.aSelect = user.getAlbum(albumname);

		album = (ObservableList<Photo>) aSelect.getPhotos(); 

		try {

			Image img = new Image(new FileInputStream(album.get(index).getFilePath()));
			imageDisplay.setImage(img);

		}catch(Exception ex) {}

		onSelect();
	}
	
	public void previousImage(final ActionEvent e) {

		try {

            int imgInd = --index; 
			Image img = new Image(new FileInputStream(album.get(imgInd).getFilePath()));
			imageDisplay.setImage(img);

		}catch(Exception ex) {}
		onSelect();
	}
	
	public void nextImage(final ActionEvent e) {

		try {

            int imgInd = ++index; 
			Image img = new Image(new FileInputStream(album.get(imgInd).getFilePath()));
			imageDisplay.setImage(img);

		}catch(Exception ex) {}
		onSelect();
	}
*/
    //finish below

    /**
     * Sets the stage for this controller. This is typically used to interact with the JavaFX window.
     * 
     * @param stage The stage associated with this controller.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Closes the slideshow view and returns to the album view.
     * 
     * @param e The action event triggering the return.
     * @throws IOException If an input or output exception occurs.
     */
	public void back2album(final ActionEvent e) throws IOException {
		stage.close();
	}

}


