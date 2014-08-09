package org.paulbetts.shroom;

import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.query.Query;

import org.paulbetts.shroom.gdrive.DriveItem;

/**
 * Created by paul on 8/5/14.
 */
public abstract class CategoryScanner {
    protected int iconId;
    public int getIconId() {
        return iconId;
    }

    protected String categoryTitle;
    public String getCategoryTitle() {
        return categoryTitle;
    }

    public abstract String generateQueryForCategory();
    public abstract PlayableRom romForDriveFile(DriveItem file);
}
