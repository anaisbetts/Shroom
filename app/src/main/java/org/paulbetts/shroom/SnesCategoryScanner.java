package org.paulbetts.shroom;
import org.paulbetts.shroom.cloudapi.FileMetadata;

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
    public PlayableRom romForDriveFile(FileMetadata file) {
        return new PlayableRom(new Date(), new Date(), file.getId(), file.getName(), false, Observable.never());
    }

    @Inject
    public SnesCategoryScanner() {}
}
