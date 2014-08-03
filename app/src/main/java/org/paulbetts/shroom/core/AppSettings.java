package org.paulbetts.shroom.core;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import org.paulbetts.shroom.LifecycleEvents;
import org.paulbetts.shroom.RxDaggerActivity;
import org.paulbetts.shroom.WelcomeActivity;

import rx.Observable;

public class AppSettings implements ActivityHelper {
    private SharedPreferences prefs;

    @Override
    public Observable<Boolean> initializeHelper(RxDaggerActivity activity) {
        prefs = activity.getSharedPreferences("Settings", 0);

        if (prefs.getBoolean("shouldShowInitialRun", true)) {
            Intent welcomeIntent = new Intent(activity, WelcomeActivity.class);

        return activity.getLifecycleFor(LifecycleEvents.CREATE)
                .flatMap(x -> activity.startObsActivityForResult(welcomeIntent))
                .map(x -> x.getValue1() == Activity.RESULT_OK)
               // .doOnNext(x -> prefs.edit().putBoolean("shouldShowInitialRun", true).commit())
                .publishLast()
                .refCount();
        } else {
            return Observable.from(Boolean.TRUE);
        }
    }
}
