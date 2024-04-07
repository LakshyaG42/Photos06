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
import java.util.HashMap;
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

/**
 * Constructs a new User with the specified username.
 * Initializes the albums list and the default tag keys.
 * 
 * @param username the username for the new user
 */
    public User(String username) {
        this.username = username;
        this.albums = new ArrayList<>();
        tagKeys = new HashSet<>();
        tagKeys.add("Location");
        tagKeys.add("Person");
        tagKeys.add("Vibe");
        
    }

/**
 * Gets the username of the user.
 * 
 * @return the username of the user
 */
    public String getUsername() {
        return username;
    }

/**
 * Sets the username of the user.
 * 
 * @param username the new username to be set
 */
    public void setUsername(String username) {
        this.username = username;
    }


/**
 * Gets the list of albums owned by the user.
 * 
 * @return the list of albums
 */
    public List<Album> getAlbums() {
        return albums;
    }

/**
 * Sets the list of albums for the user.
 * 
 * @param albums the new list of albums to be set
 */
    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

/**
 * Adds an album to the user's list of albums.
 * 
 * @param album the album to add
 */
    public void addAlbum(Album album) {
        this.albums.add(album);
    }

/**
 * Removes an album from the user's list of albums.
 * 
 * @param album the album to remove
 */
    public void removeAlbum(Album album) {
        this.albums.remove(album);
    }

/**
 * Renames a given album.
 * 
 * @param album the album to rename
 * @param newName the new name for the album
 */
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


/**
 * Loads a user from serialized data based on the username provided.
 * 
 * @param username the username of the user to load
 * @return the user if found, null otherwise
 */
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

/**
 * Retrieves a specific album by its name.
 * 
 * @param albumName the name of the album to retrieve
 * @return the album if found, null otherwise
 */
    public Album getAlbum(String albumName) {
        for (Album album : albums) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }
        return null; 
    }    

/**
 * Retrieves all unique tag keys used across all photos in all albums.
 * 
 * @return a set of all tag keys
 */
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

/**
 * Adds a tag key to the user's set of tags, specifying whether multiple values are allowed for this tag.
 * 
 * @param tagKey the tag key to add
 * @param allowMultipleValues true if multiple values are allowed for this tag key, false otherwise
 */
    public void addTagkey(String tagKey, Boolean allowMultipleValues) {
        if(this.tagKeys == null){
            this.tagKeys = new HashSet<>();
            Set<String> nullset = this.getAllTagKeys();
        }
        if (this.allowMultipleValuesMap == null) {
            this.allowMultipleValuesMap = new HashMap<>();
        }
        tagKeys.add(tagKey);
        allowMultipleValuesMap.put(tagKey, allowMultipleValues);
    }

/**
 * Determines if multiple values are allowed for a given tag key.
 * 
 * @param tagKey the tag key to check
 * @return true if multiple values are allowed for the tag key, false otherwise
 */
    public boolean isallowMultipleValues(String tagKey) {
        if (allowMultipleValuesMap.containsKey(tagKey)) {
            return allowMultipleValuesMap.get(tagKey);
        }
        return false;
    }
}
