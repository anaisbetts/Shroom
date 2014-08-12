package org.paulbetts.shroom;

import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveId;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

import org.paulbetts.shroom.core.OAuthTokenMixin;
import org.paulbetts.shroom.gdrive.DriveItem;
import org.paulbetts.shroom.gdrive.FileList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by paul on 8/9/14.
 */
@Singleton
public class CategoryScanners {
    CategoryScanner snesScanner;

    @Inject
    OAuthTokenMixin oAuthToken;

    @Inject
    public CategoryScanners(SnesCategoryScanner snes) {
        snesScanner = snes;
    }

    public Iterable<CategoryScanner> get() {
        return Arrays.asList(new CategoryScanner[]{snesScanner,});
    }

    public Observable<List<DriveItem>> performFullScan() {
        Observable<FileList> scans = Observable.merge(
                Observable.from(get())
                        .map(x -> oAuthToken.driveService.search(x.generateQueryForCategory())),
                4);

        Multimap<String, DriveItem> map = ArrayListMultimap.create();
        Observable<Set<String>> folders = scans.reduce(map, (acc, x) -> {
            for (DriveItem current : x.getItems()) {
                acc.put(current.getParents().get(0).getId(), current);
            }

            return acc;
        }).map(x -> x.keySet());

        return Observable.merge(
                folders.flatMap(x -> Observable.from(x))
                        .map(x -> oAuthToken.driveService.getItem(x)),
                4)
                .toList();

    }
}
