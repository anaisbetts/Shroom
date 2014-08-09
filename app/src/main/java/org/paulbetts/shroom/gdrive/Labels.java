package org.paulbetts.shroom.gdrive;
import com.google.gson.annotations.Expose;

public class Labels {
    @Expose
    private Boolean starred;
    @Expose
    private Boolean hidden;
    @Expose
    private Boolean trashed;
    @Expose
    private Boolean restricted;
    @Expose
    private Boolean viewed;

    public Boolean getStarred() {
        return starred;
    }

    public void setStarred(Boolean starred) {
        this.starred = starred;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getTrashed() {
        return trashed;
    }

    public void setTrashed(Boolean trashed) {
        this.trashed = trashed;
    }

    public Boolean getRestricted() {
        return restricted;
    }

    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }

    public Boolean getViewed() {
        return viewed;
    }

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }

}
