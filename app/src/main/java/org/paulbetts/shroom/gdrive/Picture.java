package org.paulbetts.shroom.gdrive;
import com.google.gson.annotations.Expose;

public class Picture {

    @Expose
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
