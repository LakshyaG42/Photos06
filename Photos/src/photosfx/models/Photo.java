package photosfx.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String filePath;
    private LocalDateTime dateTaken; 
    private String caption;
    private Map<String, String> tags; 

    public Photo(String filePath, LocalDateTime dateTaken, String caption) {
        this.filePath = filePath;
        this.dateTaken = dateTaken;
        this.caption = caption;
        this.tags = new HashMap<>();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(LocalDateTime dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public void addTag(String tagName, String tagValue) {
        tags.put(tagName, tagValue);
    }

    public void removeTag(String tagName) {
        tags.remove(tagName);
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