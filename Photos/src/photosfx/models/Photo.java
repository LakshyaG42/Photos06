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
    private List<Tags> tags; 

    public Photo(String filePath, LocalDateTime dateTaken, String caption) {
        this.filePath = filePath;
        this.dateTaken = dateTaken;
        this.caption = caption;
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

    public HashMap<String, String> getTags() {
        HashMap<String, String> tagMap = new HashMap<>();
        for (Tags tag : tags) {
            if (tag.getValues() instanceof List) {
                List<String> values = (List<String>) tag.getValues();
                String joinedValues = String.join(", ", values);
                tagMap.put(tag.getName(), joinedValues);
            } else {
                tagMap.put(tag.getName(), tag.getValues().toString());
            }
        }
        return tagMap;
    }


    public ObservableList<String> getTagsAsList() {
        ObservableList<String> tagsList = FXCollections.observableArrayList();
        HashMap<String, String> tagsMap = getTags();
        for (Map.Entry<String, String> entry : tagsMap.entrySet()) {
            //combine into single string for printing in controller
            String tagString = entry.getKey() + ": " + entry.getValue();
            tagsList.add(tagString);
        }
        return tagsList;
    }
    

    public void addTag(Tags tag) {
        tags.add(tag);
    }

    public void removeTag(Tags tag) {
        tags.remove(tag);
    }

    public static LocalDateTime getLastModifiedDateTime(File file) {
        try {
            BasicFileAttributes attributes = java.nio.file.Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            Instant lastModifiedInstant = attributes.lastModifiedTime().toInstant();
            return LocalDateTime.ofInstant(lastModifiedInstant, ZoneId.systemDefault());
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception as needed
        }
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