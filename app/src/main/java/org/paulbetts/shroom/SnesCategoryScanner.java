package org.paulbetts.shroom;

import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import org.paulbetts.shroom.gdrive.DriveItem;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by paul on 8/9/14.
 */

@Singleton
public class SnesCategoryScanner extends CategoryScanner {
    @Override
    public String generateQueryForCategory() {
        return "title contains '.smc'";
    }

    @Override
    public PlayableRom romForDriveFile(DriveItem file) {
        return new PlayableRom(new Date(), new Date(), file.getId(), file.getTitle(), false, Observable.never());
    }

    @Inject
    public SnesCategoryScanner() {}
}
