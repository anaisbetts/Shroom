package org.paulbetts.shroom.core;

import android.content.Intent;

import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.query.Query;

/**
 * Created by paul on 8/5/14.
 */
public abstract class CategoryBase {
    protected int iconId;
    public int getIconId() {
        return iconId;
    }

    protected String categoryTitle;
    public String getCategoryTitle() {
        return categoryTitle;
    }

    public abstract Query generateQueryForCategory();
    public abstract Iterable<PlayableRom> loadRomsForCategory(AppSettings settings);
}
