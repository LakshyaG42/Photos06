package photosfx.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.Serializable;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Set;

/**
 * Photo Model
 * @author Dhruv Shidhaye
 * @author Lakshya Gour
 */


public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String filePath;
    private LocalDateTime dateTaken; 
    private String caption;
    private Tags tags; 

    /**
     * Constructs a new Photo with the specified file path, date, and caption.
     * 
     * @param filePath the file path to the photo
     * @param dateTaken the date and time the photo was taken
     * @param caption the caption of the photo
     */
    public Photo(String filePath, LocalDateTime dateTaken, String caption) {
        this.filePath = filePath;
        this.dateTaken = dateTaken;
        this.caption = caption;
        this.tags = new Tags();
    }

     /**
     * Constructs a new Photo with the specified file path, date, caption, and tags.
     * 
     * @param filePath the file path to the photo
     * @param dateTaken the date and time the photo was taken
     * @param caption the caption of the photo
     * @param tags the tags associated with the photo
     */
    public Photo(String filePath, LocalDateTime dateTaken, String caption, Tags tags) {
        this.filePath = filePath;
        this.dateTaken = dateTaken;
        this.caption = caption;
        this.tags = tags;
    }

    /**
    * Gets the file path of the photo.
    * 
    * @return the file path of the photo as a String
    */
    public String getFilePath() {
        return filePath;
    }

    /**
    * Sets the file path of the photo.
    * 
     * @param filePath the new file path for the photo
    */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the date and time when the photo was taken.
    * 
    * @return the date and time of the photo as LocalDateTime
    */
    public LocalDateTime getDateTime() {
        return dateTaken;
    }

    /**
    * Sets the date and time when the photo was taken.
    * 
    * @param dateTaken the new date and time of the photo
    */
    public void setDateTime(LocalDateTime dateTaken) {
        this.dateTaken = dateTaken;
    }

    /**
 * Gets the caption of the photo.
 * 
 * @return the caption of the photo as a String
 */
    public String getCaption() {
        return caption;
    }

    /**
 * Sets the caption of the photo.
 * 
 * @param caption the new caption for the photo
 */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
 * Gets all tags associated with the photo.
 * 
 * @return a map of tag names to sets of tag values
 */
    public Map<String, Set<String>> getTags() {
      return tags.getTagMap();
    }

    /**
 * Gets the tags object associated with the photo.
 * 
 * @return the tags object
 */
    public Tags getTag() {
        return tags;
    }

/**
 * Gets a list of tags in a format suitable for display.
 * 
 * @return an observable list of tag strings
 */
    public ObservableList<String> getTagsAsList() {
        ObservableList<String> tagsList = FXCollections.observableArrayList();
        for (Map.Entry<String, Set<String>> entry : getTags().entrySet()) {
            String tagName = entry.getKey();
            Set<String> tagValues = entry.getValue();
            for (String tagValue : tagValues) {
                tagsList.add(tagName + ": " + tagValue);
            }
        }
        return tagsList;
    }
    

    /**
     * Adds a tag to the photo.
     * 
     * @param tagName the name of the tag
     * @param tagValue the value of the tag
     * @param allowMultipleValues whether multiple values are allowed for this tag
     */
    public void addTag(String tagName, String tagValue, boolean allowMultipleValues) {
        tags.addTag(tagName, tagValue, allowMultipleValues);
    }

    /**
     * Adds a set of tag values to the photo under a single tag name.
     * 
     * @param tagName the name of the tag
     * @param tagValue a set of tag values
     * @param allowMultipleValues whether multiple values are allowed for this tag
     */
    public void addTag(String tagName, Set<String> tagValue, boolean allowMultipleValues) {
        tags.addTag(tagName, tagValue, allowMultipleValues);
    }

    /**
     * Removes a tag from the photo.
     * 
     * @param tagName the name of the tag to remove
     * @param tagValue the value of the tag to remove
     */
    public void removeTag(String tagName, String tagValue) {
        tags.removeTag(tagName, tagValue);
    }

    /**
     * Retrieves the last modified date and time of a file.
     * 
     * @param file the file to retrieve the last modified date and time for
     * @return a LocalDateTime object representing the last modified date and time
     */
    public static LocalDateTime getLastModifiedDateTime(File file) {
        try {
            BasicFileAttributes attributes = java.nio.file.Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            Instant lastModifiedInstant = attributes.lastModifiedTime().toInstant();
            return LocalDateTime.ofInstant(lastModifiedInstant, ZoneId.systemDefault());
        } catch (Exception e) {
            e.printStackTrace(); 
        }
        System.out.println("Error getting last modified date time for file: " + file.getAbsolutePath());
        return null;
    }

    /**
     * Provides a string representation of the photo including its path, date taken, caption, and tags.
     * 
     * @return a string representation of the photo
     */
    
    @Override
    public String toString() {
        return "Photo{" +
                "filePath='" + filePath + '\'' +
                ", dateTaken=" + dateTaken +
                ", caption='" + caption + '\'' +
                ", tags=" + tags +
                '}';
    }
}