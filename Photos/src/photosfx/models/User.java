package photosfx.models;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import photosfx.models.Admin;
import photosfx.models.Album;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * User Model
 * @author Dhruv Shidhaye
 * @author Lakshya Gour
 */

public class User implements Serializable{
    private static final long serialVersionUID = 1L;

    private String username;
    private List<Album> albums;
    private Set<String> tagKeys;
    private Map<String, Boolean> allowMultipleValuesMap;
    public User(String username) {
        this.username = username;
        this.albums = new ArrayList<>();
        tagKeys = new HashSet<>();
        tagKeys.add("Location");
        tagKeys.add("Person");
        tagKeys.add("Vibe");
        
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

    // public void serializeAlbumNames() {
    //     try {
    //         FileOutputStream fileOut = new FileOutputStream("Photos/data/userPhotos/" + username + "_albums.ser");
    //         ObjectOutputStream out = new ObjectOutputStream(fileOut);
    //         List<String> albumNames = new ArrayList<>();
    //         for (Album album : albums) {
    //             albumNames.add(album.getName());
    //         }
    //         out.writeObject(albumNames);
    //         out.close();
    //         fileOut.close();
    //     } catch (IOException i) {
    //         i.printStackTrace();
    //     }
    // }

    public static User loadUser(String username) {
        try {
            Admin.loadUsers("Photos/data/users.ser");
            for (User user : Admin.getUsers()) {
                if (user.getUsername().equals(username)) {
                    return user;
                }
            }
        } catch (Exception e) {
            // End of file reached, user not found
            Admin.showAlert(Alert.AlertType.ERROR, "Error loading users:", e.getMessage());
            System.err.println("Error loading users: " + e.getMessage());
        }
        return null;
    }

    public Album getAlbum(String albumName) {
        for (Album album : albums) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }
        return null; 
    }    

    public Set<String> getAllTagKeys() {
        if(this.tagKeys == null){
            this.tagKeys = new HashSet<>();
        }
        for(Album album : albums) {
            for(Photo photo : album.getPhotos()) {
                this.tagKeys.addAll(photo.getTags().keySet());
            }
        }
        return tagKeys;
    }

    public void addTagkey(String tagKey, Boolean allowMultipleValues) {
        if(this.tagKeys == null){
            this.tagKeys = new HashSet<>();
            Set<String> nullset = this.getAllTagKeys();
        }
        tagKeys.add(tagKey);
        allowMultipleValuesMap.put(tagKey, allowMultipleValues);
    }

    public boolean isallowMultipleValues(String tagKey) {
        return allowMultipleValuesMap.get(tagKey);
    }
}
