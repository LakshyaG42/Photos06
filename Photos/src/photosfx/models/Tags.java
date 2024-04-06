package photosfx.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tags Model
 * @author Dhruv Shidhaye
 * @author Lakshya Gour
 */

public class Tags implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Set<String>> tagMap; 
    public Tags() {
        this.tagMap = new HashMap<>();
    }
    public void addTag(String tagName, String tagValue) {
        tagMap.computeIfAbsent(tagName, k -> new HashSet<>()).add(tagValue);
    }

    public void addTag(String tagName, Set<String> tagValues) {
        tagMap.put(tagName, new HashSet<>(tagValues));
    }
    public void removeTag(String tagName, Set<String> tagValues) {
        if(tagMap.containsKey(tagName)) {
            Set<String> values = tagMap.get(tagName);
            values.removeAll(tagValues);
            if(values.isEmpty()) {
                tagMap.remove(tagName);
            }
        }
    }
    public Map<String, Set<String>> getTagMap() {
        return tagMap;
    }
    public Set<String> getTagNames() {
        return tagMap.keySet();
    }
    public Set<String> getTagValues(String tagName) {
        return tagMap.getOrDefault(tagName, new HashSet<>());
    }
    public boolean hasTag(String tagName) {
        return tagMap.containsKey(tagName);
    }
    public boolean hasTagValue(String tagName, String tagValue) {
        Set<String> values = tagMap.get(tagName);
        return values != null && values.contains(tagValue);
    }
    public void clearTags() {
        tagMap.clear();
    }
    
}
