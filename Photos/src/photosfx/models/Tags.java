package photosfx.models;

import java.util.ArrayList;
import java.util.List;

public class Tags {
    private String tagName;

    private List<String> values;

    private boolean allowMultipleValues;

    public Tags(String tagName, boolean allowMultipleValues) {
        this.tagName = tagName;
        this.allowMultipleValues = allowMultipleValues;
        this.values = new ArrayList<>();
    }
    public String getName() {
        return tagName;
    }

    public boolean isAllowMultipleValues() {
        return allowMultipleValues;
    }

    public List<String> getValues() {
        return values;
    }

    public void addValue(String value) {
        if (allowMultipleValues) {
            values.add(value);
        } else {
            values.clear();
            values.add(value);
        }
    }

    public void removeValue(String value) {
        values.remove(value);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + tagName + '\'' +
                ", values=" + values +
                ", allowMultipleValues=" + allowMultipleValues +
                '}';
    }
}
