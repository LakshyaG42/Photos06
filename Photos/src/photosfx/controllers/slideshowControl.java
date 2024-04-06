package photosfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.*;
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

    //finish below
	public void back2album(final ActionEvent e) throws IOException {
		back2album(e); 
	}

}


