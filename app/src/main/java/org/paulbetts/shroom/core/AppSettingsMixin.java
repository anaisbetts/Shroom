package org.paulbetts.shroom.core;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import org.paulbetts.shroom.WelcomeActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

public class AppSettingsMixin implements ElementMixin {
    private SharedPreferences prefs;

    @Inject
    RxDaggerActivity hostActivity;

    @Inject
    public AppSettingsMixin() { }

    @Override
    public Observable<Boolean> initializeHelper(RxDaggerElement host) {
        prefs = hostActivity.getSharedPreferences("Settings", 0);

        if (prefs.getBoolean("shouldShowInitialRun", true) && hostActivity.getClass() != WelcomeActivity.class) {
            Intent welcomeIntent = new Intent(hostActivity, WelcomeActivity.class);

            return Lifecycle.getLifecycleFor(hostActivity, LifecycleEvents.CREATE).take(1)
                    .flatMap(x -> hostActivity.startObsActivityForResult(welcomeIntent, android.R.anim.fade_in, android.R.anim.fade_out))
                    .map(x -> x.getValue0() == Activity.RESULT_OK)
                    .doOnNext(resultIsOk -> prefs.edit().putBoolean("shouldShowInitialRun", resultIsOk == false).commit())
                    .publishLast()
                    .refCount();
        } else {
            return Observable.from(Boolean.TRUE);
        }
    }

    String getGDriveOAuthToken() {
        return prefs.getString("oauthToken", null);
    }

    void setGDriveOAuthToken(String newToken) {
        prefs.edit().putString("oauthToken", newToken).commit();
    }
}
