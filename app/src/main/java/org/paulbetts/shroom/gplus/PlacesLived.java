package org.paulbetts.shroom.gplus;
import com.google.gson.annotations.Expose;

public class PlacesLived {

    @Expose
    private String value;
    @Expose
    private Boolean primary;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

}
