package org.paulbetts.shroom.gplus;
import com.google.gson.annotations.Expose;

public class Email {
    @Expose
    private String value;
    @Expose
    private String type;

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
}
