package org.paulbetts.shroom;

import org.paulbetts.shroom.core.OAuthTokenMixin;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

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

    public Observable<List<PlayableRom>> performFullScan() {
        return null;
    }
}
