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
    private Map<String, Boolean> allowMultipleValuesMap;

    public Tags() {
        this.tagMap = new HashMap<>();
        setAllowMultipleValues("Location", false);
        setAllowMultipleValues("Person", true);
        setAllowMultipleValues("Vibes", true);
    }
    public void addTag(String tagName, String tagValue, boolean allowMultipleValues) {
        tagMap.computeIfAbsent(tagName, k -> new HashSet<>()).add(tagValue);
        allowMultipleValuesMap.put(tagName, allowMultipleValues);
    }
    public void addTag(String tagName, String tagValue) {
        if(allowMultipleValues(tagName) || !hasTag(tagName)) {
            tagMap.computeIfAbsent(tagName, k -> new HashSet<>()).add(tagValue);
        } 
    }
    public void addTag(String tagName, Set<String> tagValue) {
        if(allowMultipleValues(tagName) || !hasTag(tagName)) {
            tagMap.computeIfAbsent(tagName, k -> new HashSet<>()).addAll(tagValue);
        } else {
            System.out.println("Tag " + tagName + " does not allow multiple values");
        }
    }
    public void addTag(String tagName, Set<String> tagValues, boolean allowMultipleValues) {
        tagMap.computeIfAbsent(tagName, k -> new HashSet<>()).addAll(tagValues);
        allowMultipleValuesMap.put(tagName, allowMultipleValues);
    }
    public void removeTag(String tagName, String tagValue) {
        if(tagMap.containsKey(tagName)) {
            Set<String> values = tagMap.get(tagName);
            values.remove(tagValue);
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
    public boolean allowMultipleValues(String tagkey) {
        if(allowMultipleValuesMap == null) allowMultipleValuesMap = new HashMap<>();
        setAllowMultipleValues("Location", false);
        setAllowMultipleValues("Person", true);
        setAllowMultipleValues("Vibe", true);
        if(allowMultipleValuesMap.get(tagkey) == null) {
            allowMultipleValuesMap.put(tagkey, false);
        }
        return allowMultipleValuesMap.get(tagkey);
    }
    public void setAllowMultipleValues(String tagkey, boolean allowMultipleValues) {
        if(allowMultipleValuesMap == null) allowMultipleValuesMap = new HashMap<>();
        allowMultipleValuesMap.put(tagkey, allowMultipleValues);
    }

}
