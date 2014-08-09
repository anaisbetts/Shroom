package org.paulbetts.shroom;

import com.google.android.gms.drive.query.Query;

import org.paulbetts.shroom.core.AppSettings;

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
