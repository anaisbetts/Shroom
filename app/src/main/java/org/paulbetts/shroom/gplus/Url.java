package org.paulbetts.shroom.gplus;
import com.google.gson.annotations.Expose;

public class Url {
    @Expose
    private String value;
    @Expose
    private String type;
    @Expose
    private String label;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}