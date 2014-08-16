package org.paulbetts.shroom;
import org.paulbetts.shroom.cloudapi.FileMetadata;

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
    public abstract PlayableRom romForDriveFile(FileMetadata file);
}
