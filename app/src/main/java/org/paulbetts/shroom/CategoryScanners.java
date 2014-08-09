package org.paulbetts.shroom;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by paul on 8/9/14.
 */
@Singleton
public class CategoryScanners {
    CategoryScanner snesScanner;

    @Inject
    public CategoryScanners(SnesCategoryScanner snes) {
        snesScanner = snes;
    }

    public Iterable<CategoryScanner> get() {
        return Arrays.asList(new CategoryScanner[]{snesScanner,});
    }
}
