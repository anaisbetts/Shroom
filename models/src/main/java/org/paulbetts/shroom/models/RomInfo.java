package org.paulbetts.shroom.models;

public class RomInfo {
    private String storageType = "dropbox";
    private String storagePath;
    private String title;
    private String description;
    private String coverArtUrl;

    public RomInfo(String title, String description, String coverArtUrl, String storagePath) {
        this.title = title;
        this.description = description;
        this.coverArtUrl = coverArtUrl;
        this.storagePath = storagePath;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverArtUrl() {
        return coverArtUrl;
    }

    public void setCoverArtUrl(String coverArtUrl) {
        this.coverArtUrl = coverArtUrl;
    }

    public String getStorageType() {
        return storageType;
    }
}
