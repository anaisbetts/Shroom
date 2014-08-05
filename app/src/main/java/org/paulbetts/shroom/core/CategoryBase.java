package org.paulbetts.shroom.core;

import com.google.android.gms.drive.DriveFile;

/**
 * Created by paul on 8/5/14.
 */
public abstract class CategoryBase {

    private int iconId;
    public int getIconId() {
        return iconId;
    }

    public abstract boolean interestedInFile(DriveFile file);
    public abstract Iterable<PlayableRom> loadRomsForCategory(AppSettings settings);
}
