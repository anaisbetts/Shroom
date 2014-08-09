package org.paulbetts.shroom;

import android.media.Image;

import com.google.android.gms.drive.DriveId;

import java.util.Date;

import rx.*;

public class PlayableRom {
    public PlayableRom(Date createdAt, Date lastPlayedAt, DriveId romDriveId, String friendlyTitle, boolean isStarred, Observable<Image> imageToBeLoaded) {
        this.createdAt = createdAt;
        this.lastPlayedAt = lastPlayedAt;
        this.romDriveId = romDriveId;
        this.friendlyTitle = friendlyTitle;
        this.isStarred = isStarred;
        this.imageToBeLoaded = imageToBeLoaded;
    }

    protected Date createdAt;
    public Date getCreatedAt() {
        return createdAt;
    }

    protected Date lastPlayedAt;
    public Date getLastPlayedAt() {
        return lastPlayedAt;
    }

    protected DriveId romDriveId;
    public DriveId getRomDriveId() {
        return romDriveId;
    }

    protected String friendlyTitle;
    public String getFriendlyTitle() {
        return friendlyTitle;
    }

    protected boolean isStarred;
    public boolean isStarred() {
        return isStarred;
    }
    public void setStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    protected Observable<Image> imageToBeLoaded;
    public Observable<Image> getImageToBeLoaded() {
        return imageToBeLoaded;
    }
}
