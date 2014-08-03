package org.paulbetts.shroom.core;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.paulbetts.shroom.LifecycleEvents;
import org.paulbetts.shroom.RxDaggerActivity;
import org.paulbetts.shroom.WelcomeActivity;

import javax.inject.Inject;

import rx.Observable;

public class AppSettings implements ActivityHelper {
    private SharedPreferences prefs;

    @Inject
    public AppSettings() {
    }

    @Override
    public Observable<Boolean> initializeHelper(RxDaggerActivity activity) {
        prefs = activity.getSharedPreferences("Settings", 0);

        if (prefs.getBoolean("shouldShowInitialRun", true)) {
            Intent welcomeIntent = new Intent(activity, WelcomeActivity.class);

            return activity.getLifecycleFor(LifecycleEvents.CREATE).take(1)
                .flatMap(x -> activity.startObsActivityForResult(welcomeIntent))
                .map(x -> x.getValue0() == Activity.RESULT_OK)
                .doOnNext(resultIsOk -> prefs.edit().putBoolean("shouldShowInitialRun", resultIsOk == false).commit())
                .publishLast()
                .refCount();
        } else {
            return Observable.from(Boolean.TRUE);
        }
    }
}
