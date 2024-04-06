package photosfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.*;
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

    private ObservableList<Photo> album;

    private int index;

    private Album aSelect;

    private User user;

    private void onSelect(){

		prevButton.setDisable(index == 0);
		nextButton.setDisable(index == album.size()-1);

	}

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

private void displayPhoto(int index) {
    try {
        Image img = new Image(new FileInputStream(album.get(index).getFilePath()));
        imageDisplay.setImage(img);
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}

public void previousImage(final ActionEvent e) {
    if (index > 0) {
        index--; 
        displayPhoto(index);
        onSelect();
    }
}

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
	public void back2album(final ActionEvent e) throws IOException {
		back2album(e); 
	}

}


