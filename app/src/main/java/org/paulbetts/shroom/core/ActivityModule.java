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
                AppSettingsMixin.class,
                OAuthTokenMixin.class,
        },
        addsTo = AndroidModule.class,
        library = true
)
public class ActivityModule {
    private RxDaggerActivity activity;
    private AppSettingsMixin appSettingsMixin = null;
    private OAuthTokenMixin oAuthTokenMixin = null;

    public ActivityModule(RxDaggerActivity activity) {
        this.activity = activity;
    }

    @Provides @Singleton RxDaggerActivity providesDaggerActivity(){
        return activity;
    }

    @Provides @Singleton AppSettingsMixin providesAppSettings() {
        if (appSettingsMixin == null) {
            appSettingsMixin = new AppSettingsMixin();
            activity.inject(appSettingsMixin);
        }
        return appSettingsMixin;
    }

    @Provides @Singleton OAuthTokenMixin providesAuthToken() {
        if (oAuthTokenMixin == null) {
            oAuthTokenMixin = new OAuthTokenMixin();
            activity.inject(oAuthTokenMixin);
        }

        return oAuthTokenMixin;
    }
}
