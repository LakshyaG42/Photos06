package photosfx.models;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public LocalDateTime getEarliestDate() {
        return earliestDate;
    }

    public void setEarliestDate(LocalDateTime earliestDate) {
        this.earliestDate = earliestDate;
    }

    public LocalDateTime getLatestDate() {
        return latestDate;
    }

    public void setLatestDate(LocalDateTime latestDate) {
        this.latestDate = latestDate;
    }
    // Additional methods for photo management

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
        updateDateRange(photo);
    }

    public void removePhoto(Photo photo) {
        this.photos.remove(photo);
        updateDateRange(photo);
    }

    private void updateDateRange(Photo photo) {
        if (earliestDate == null || photo.getDateTime().isBefore(earliestDate)) {
            earliestDate = photo.getDateTime();
        }
        if (latestDate == null || photo.getDateTime().isAfter(latestDate)) {
            latestDate = photo.getDateTime();
        }
    }
}
