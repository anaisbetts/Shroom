package org.paulbetts.shroom.core;

import android.app.Activity;

import org.paulbetts.shroom.RxDaggerActivity;

import rx.Observable;

public interface ActivityHelper {
    Observable<Boolean> initializeHelper(RxDaggerActivity activity);
}
