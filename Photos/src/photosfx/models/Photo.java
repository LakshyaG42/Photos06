package photosfx.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.Serializable;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
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

    public Photo(String filePath, LocalDateTime dateTaken, String caption) {
        this.filePath = filePath;
        this.dateTaken = dateTaken;
        this.caption = caption;
        this.tags = new Tags();
    }
    public Photo(String filePath, LocalDateTime dateTaken, String caption, Tags tags) {
        this.filePath = filePath;
        this.dateTaken = dateTaken;
        this.caption = caption;
        this.tags = tags;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getDateTime() {
        return dateTaken;
    }

    public void setDateTime(LocalDateTime dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Map<String, Set<String>> getTags() {
      return tags.getTagMap();
    }


    public ObservableList<String> getTagsAsList() {
        ObservableList<String> tagsList = FXCollections.observableArrayList();
        for (Map.Entry<String, Set<String>> entry : getTags().entrySet()) {
            String tagName = entry.getKey();
            Set<String> tagValues = entry.getValue();
            StringBuilder tagString = new StringBuilder(tagName + ": ");
            if (tagValues != null && !tagValues.isEmpty()) {
                for (String tagValue : tagValues) {
                    tagString.append(tagValue).append(", ");
                }
                tagString.setLength(tagString.length() - 2); // Remove the last comma and space
            }
            tagsList.add(tagString.toString());
        }
        return tagsList;
    }
    

    public void addTag(String tagName, String tagValue) {
        tags.addTag(tagName, tagValue);
    }
    public void addTag(String tagName, Set<String> tagValue) {
        tags.addMultipleValuesTag(tagName, tagValue);
    }

    public void removeTag(String tagName, Set<String> tagValue) {
        tags.removeTag(tagName, tagValue);
    }

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