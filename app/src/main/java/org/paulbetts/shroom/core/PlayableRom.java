package org.paulbetts.shroom.core;

import android.media.Image;

import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import rx.*;

public class PlayableRom {
    public PlayableRom(DriveId romDriveId, String friendlyTitle, boolean isStarred, Observable<Image> imageToBeLoaded) {
        this.romDriveId = romDriveId;
        this.friendlyTitle = friendlyTitle;
        this.isStarred = isStarred;
        this.imageToBeLoaded = imageToBeLoaded;
    }

    private DriveId romDriveId;
    public DriveId getRomDriveId() {
        return romDriveId;
    }

    private String friendlyTitle;
    public String getFriendlyTitle() {
        return friendlyTitle;
    }

    private boolean isStarred;
    public boolean isStarred() {
        return isStarred;
    }
    public void setStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    private Observable<Image> imageToBeLoaded;
    public Observable<Image> getImageToBeLoaded() {
        return imageToBeLoaded;
    }
}
