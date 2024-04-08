package photosfx.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
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

      /**
     * Constructs a new Tags object with default settings.
     */
    public Tags() {
        this.tagMap = new HashMap<>();
        setAllowMultipleValues("Location", false);
        setAllowMultipleValues("Person", true);
        setAllowMultipleValues("Vibes", true);
    }

    /**
     * Adds a new tag with a single value. Optionally allows multiple values for this tag.
     *
     * @param tagName the name of the tag to add
     * @param tagValue the value of the tag to add
     * @param allowMultipleValues true if the tag can have multiple values
     */
    public void addTag(String tagName, String tagValue, boolean allowMultipleValues) {
        tagMap.computeIfAbsent(tagName, k -> new HashSet<>()).add(tagValue);
        allowMultipleValuesMap.put(tagName, allowMultipleValues);
    }

    /**
     * Adds a new tag with a single value. Assumes the allowMultipleValues based on existing settings.
     *
     * @param tagName the name of the tag to add
     * @param tagValue the value of the tag to add
     */
    public void addTag(String tagName, String tagValue) {
        if(allowMultipleValues(tagName) || !hasTag(tagName)) {
            tagMap.computeIfAbsent(tagName, k -> new HashSet<>()).add(tagValue);
        } 
    }

    /**
     * Adds a new tag with multiple values. Assumes the allowMultipleValues based on existing settings.
     *
     * @param tagName the name of the tag to add
     * @param tagValue the set of values for the tag to add
     */
    public void addTag(String tagName, Set<String> tagValue) {
        if(allowMultipleValues(tagName) || !hasTag(tagName)) {
            tagMap.computeIfAbsent(tagName, k -> new HashSet<>()).addAll(tagValue);
        } else {
            System.out.println("Tag " + tagName + " does not allow multiple values");
        }
    }

    /**
     * Adds a new tag with multiple values. Optionally allows multiple values for this tag.
     *
     * @param tagName the name of the tag to add
     * @param tagValues the set of values for the tag to add
     * @param allowMultipleValues true if the tag can have multiple values
     */
    public void addTag(String tagName, Set<String> tagValues, boolean allowMultipleValues) {
        tagMap.computeIfAbsent(tagName, k -> new HashSet<>()).addAll(tagValues);
        allowMultipleValuesMap.put(tagName, allowMultipleValues);
    }

    /**
     * Removes a specific value from a tag.
     *
     * @param tagName the name of the tag
     * @param tagValue the value to remove
     */
    public void removeTag(String tagName, String tagValue) {
        if(tagMap.containsKey(tagName)) {
            Set<String> values = tagMap.get(tagName);
            values.remove(tagValue);
            if(values.isEmpty()) {
                tagMap.remove(tagName);
            }
        }
    }

    /**
     * Gets the entire map of tags and their associated values.
     *
     * @return a map from tag names to sets of tag values
     */
    public Map<String, Set<String>> getTagMap() {
        return tagMap;
    }

     /**
     * Retrieves a set of all tag names.
     *
     * @return a set of tag names
     */
    public Set<String> getTagNames() {
        return tagMap.keySet();
    }

    /**
     * Retrieves a set of values for a given tag name.
     *
     * @param tagName the name of the tag
     * @return a set of values for the tag, or an empty set if the tag does not exist
     */
    public Set<String> getTagValues(String tagName) {
        return tagMap.getOrDefault(tagName, new HashSet<>());
    }

     /**
     * Checks if the tag with the given name exists.
     *
     * @param tagName the name of the tag to check
     * @return true if the tag exists, false otherwise
     */
    public boolean hasTag(String tagName) {
        return tagMap.containsKey(tagName);
    }

     /**
     * Checks if a specific value exists for the given tag name.
     *
     * @param tagName the name of the tag
     * @param tagValue the value to check for
     * @return true if the value exists for the tag, false otherwise
     */
    public boolean hasTagValue(String tagName, String tagValue) {
        Set<String> values = tagMap.get(tagName);
        return values != null && values.contains(tagValue);
    }

    /**
     * Removes all tags and their associated values.
     */
    public void clearTags() {
        tagMap.clear();
    }

    /**
     * Checks if multiple values are allowed for a given tag key.
     *
     * @param tagkey the key of the tag
     * @return true if multiple values are allowed, false otherwise
     */
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

    /**
     * Sets whether multiple values are allowed for a given tag key.
     *
     * @param tagkey the key of the tag
     * @param allowMultipleValues true to allow multiple values, false to allow only one
     */
    public void setAllowMultipleValues(String tagkey, boolean allowMultipleValues) {
        if(allowMultipleValuesMap == null) allowMultipleValuesMap = new HashMap<>();
        allowMultipleValuesMap.put(tagkey, allowMultipleValues);
    }

}
