package org.paulbetts.shroom.gdrive;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class FileList {

    @Expose
    private String kind;
    @Expose
    private String etag;
    @Expose
    private String selfLink;
    @Expose
    private List<DriveItem> driveItems = new ArrayList<DriveItem>();

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public List<DriveItem> getDriveItems() {
        return driveItems;
    }

    public void setDriveItems(List<DriveItem> driveItems) {
        this.driveItems = driveItems;
    }
}
