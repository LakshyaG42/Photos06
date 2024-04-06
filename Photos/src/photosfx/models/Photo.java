package photosfx.models;

import java.io.Serializable;
import java.time.LocalDateTime;
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


    public void addTag(Tags tag) {
        tags.add(tag);
    }

    public void removeTag(Tags tag) {
        tags.remove(tag);
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