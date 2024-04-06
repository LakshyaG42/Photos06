package photosfx.models;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import photosfx.models.Admin;
import photosfx.models.Album;
import javafx.scene.control.Alert;

import java.util.ArrayList;


/**
 * User Model
 * @author Dhruv Shidhaye
 * @author Lakshya Gour
 */

public class User implements Serializable{
    private static final long serialVersionUID = 1L;

    private String username;
    private List<Album> albums;

    public User(String username) {
        this.username = username;
        this.albums = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public void addAlbum(Album album) {
        this.albums.add(album);
    }

    public void removeAlbum(Album album) {
        this.albums.remove(album);
    }

    public void renameAlbum(Album album, String newName) {
        album.setName(newName);
    }

    public void serializeAlbumNames() {
        try {
            FileOutputStream fileOut = new FileOutputStream("Photos/data/userPhotos/" + username + "_albums.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            List<String> albumNames = new ArrayList<>();
            for (Album album : albums) {
                albumNames.add(album.getName());
            }
            out.writeObject(albumNames);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }


    public static User loadUser(String username) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Photos/data/userPhotos/" + username + ".ser"))) {
            User user = (User) inputStream.readObject();
            return user;
        } catch (IOException | ClassNotFoundException e) {
            Admin.showAlert(Alert.AlertType.ERROR, "Error loading users:", e.getMessage());
            System.err.println("Error loading users: " + e.getMessage());
            return null;
        }
    }
}
