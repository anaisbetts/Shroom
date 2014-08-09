package org.paulbetts.shroom.gplus;

import com.google.gson.annotations.Expose;

public class Image {
    @Expose
    private String url;
    @Expose
    private Boolean isDefault;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
}
