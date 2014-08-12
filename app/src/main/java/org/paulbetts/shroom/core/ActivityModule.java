package org.paulbetts.shroom.core;

import org.paulbetts.shroom.CategoryDetailActivity;
import org.paulbetts.shroom.CategoryListActivity;
import org.paulbetts.shroom.WelcomeActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by paul on 8/12/14.
 */
@Module(
        injects = {
                CategoryListActivity.class,
                CategoryDetailActivity.class,
                WelcomeActivity.class,
        },
        addsTo = AndroidModule.class,
        library = true
)
public class ActivityModule {
    private final RxDaggerActivity activity;
    private final AppSettingsMixin appSettingsMixin;

    public ActivityModule(RxDaggerActivity activity) {
        this.activity = activity;
        appSettingsMixin = new AppSettingsMixin();
    }

    @Provides @Singleton RxDaggerActivity providesDaggerActivity(){
        return activity;
    }

    @Provides @Singleton AppSettingsMixin providesAppSettings() {
        return appSettingsMixin;
    }
}
