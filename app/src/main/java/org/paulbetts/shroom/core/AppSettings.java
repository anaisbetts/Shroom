package org.paulbetts.shroom.core;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;

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

        if (prefs.getBoolean("shouldShowInitialRun", true) && activity.getClass() != WelcomeActivity.class) {
            Intent welcomeIntent = new Intent(activity, WelcomeActivity.class);

            return activity.getLifecycleFor(LifecycleEvents.CREATE).take(1)
                    .flatMap(x -> activity.startObsActivityForResult(welcomeIntent, android.R.anim.fade_in, android.R.anim.fade_out))
                    .map(x -> x.getValue0() == Activity.RESULT_OK)
                    .doOnNext(resultIsOk -> prefs.edit().putBoolean("shouldShowInitialRun", resultIsOk == false).commit())
                    .publishLast()
                    .refCount();
        } else {
            return Observable.from(Boolean.TRUE);
        }
    }

    public DriveFolder getRootRomFolder(GoogleApiClient apiClient) {
        String s = prefs.getString("rootRomFolder", null);
        if (s == null) return null;

        return Drive.DriveApi.getFolder(apiClient, DriveId.decodeFromString(s));
    }

    public void setRootRomFolder(DriveFolder folder) {
        prefs.edit()
                .putString("rootRomFolder", folder.getDriveId().encodeToString())
                .apply();
    }
}
