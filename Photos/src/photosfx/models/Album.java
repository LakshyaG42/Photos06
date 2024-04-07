package photosfx.models;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;

/**
 * Album Model
 * @author Dhruv Shidhaye
 * @author Lakshya Gour
 */

public class Album implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String name;
    private List<Photo> photos;

    private LocalDateTime earliestDate;
    private LocalDateTime latestDate;

    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<>();
    }

    // Getters and setters

    /**
 * Gets the name of the album.
 * 
 * @return the name of the album
 */

    public String getName() {
        return name;
    }

    /**
 * Sets the name of the album.
 * 
 * @param name the new name of the album
 */
    public void setName(String name) {
        this.name = name;
    }

    /**
 * Gets the list of photos in the album.
 * 
 * @return the list of photos
 */
    public List<Photo> getPhotos() {
        return photos;
    }

    /**
 * Sets the list of photos in the album.
 * 
 * @param photos the new list of photos for the album
 */
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    /**
 * Gets the earliest date from all the photos in the album.
 * 
 * @return the earliest date or null if no photos
 */
    public LocalDateTime getEarliestDate() {
        return earliestDate;
    }


    /**
 * Sets the earliest date of the photos in the album.
 * 
 * @param earliestDate the earliest date of the photos
 */
    public void setEarliestDate(LocalDateTime earliestDate) {
        this.earliestDate = earliestDate;
    }

    /**
 * Gets the latest date from all the photos in the album.
 * 
 * @return the latest date or null if no photos
 */
    public LocalDateTime getLatestDate() {
        return latestDate;
    }

    /**
 * Sets the latest date of the photos in the album.
 * 
 * @param latestDate the latest date of the photos
 */
    public void setLatestDate(LocalDateTime latestDate) {
        this.latestDate = latestDate;
    }
    // Additional methods for photo management

    /**
 * Adds a photo to the album and updates the date range.
 * 
 * @param photo the photo to add
 */
    public void addPhoto(Photo photo) {
        this.photos.add(photo);
        updateDateRange(photo);
    }

    /**
 * Removes a photo from the album and updates the date range.
 * 
 * @param photo the photo to remove
 */
    public void removePhoto(Photo photo) {
        this.photos.remove(photo);
        updateDateRange(photo);
    }

    /**
 * Updates the earliest and latest dates of the album based on a new photo's date.
 * 
 * @param photo the photo with the date to update from
 */

    private void updateDateRange(Photo photo) {
        if (earliestDate == null || photo.getDateTime().isBefore(earliestDate)) {
            earliestDate = photo.getDateTime();
        }
        if (latestDate == null || photo.getDateTime().isAfter(latestDate)) {
            latestDate = photo.getDateTime();
        }
    }

    /**
 * Copies a photo to another album if it doesn't already exist there.
 * 
 * @param filename the file name of the photo to copy
 * @param album the destination album
 */
    public void copyPhoto(String filename, Album album) {
        for (Photo photo : album.getPhotos()) {
            if (photo.getFilePath().equals(filename)) {
                Admin.showAlert(Alert.AlertType.ERROR, "Error", "Photo already exists in the destination album.");
                return;
            }
        }
        for (Photo photo : photos) {
            if (photo.getFilePath().equals(filename)) {
                album.addPhoto(photo);
                return;
            }
        }
    }

    /**
 * Moves a photo to another album if it doesn't already exist there.
 * 
 * @param filename the file name of the photo to move
 * @param albumTo the destination album
 */
    public void movePhoto(String filename, Album albumTo) {
        for (Photo photo : albumTo.getPhotos()) {
            if (photo.getFilePath().equals(filename)) {
                Admin.showAlert(Alert.AlertType.ERROR, "Error", "Photo already exists in the destination album.");
                return;
            }
        }
        for (Photo photo : photos) {
            
            if (photo.getFilePath().equals(filename)) {
                albumTo.addPhoto(photo);
                photos.remove(photo);
                return;
            }
        }
    }
}
