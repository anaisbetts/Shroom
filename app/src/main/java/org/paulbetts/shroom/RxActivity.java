package org.paulbetts.shroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.javatuples.Triplet;

import rx.subjects.PublishSubject;
import rx.*;

/**
 * Created by paul on 8/1/14.
 */
public class RxActivity extends Activity {

    PublishSubject<LifecycleEvents> lifecycleEvents = PublishSubject.create();
    public Observable<LifecycleEvents> getLifecycleEvents() {
        return lifecycleEvents;
    }

    PublishSubject<Triplet<Integer, Integer, Intent>> activityResult = PublishSubject.create();
    public Observable<Triplet<Integer, Integer, Intent>> getActivityResult() {
        return activityResult;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activityResult.onNext(Triplet.with(requestCode, resultCode, data));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleEvents.onNext(LifecycleEvents.CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleEvents.onNext(LifecycleEvents.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleEvents.onNext(LifecycleEvents.RESUME);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        lifecycleEvents.onNext(LifecycleEvents.RESTART);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifecycleEvents.onNext(LifecycleEvents.PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifecycleEvents.onNext(LifecycleEvents.STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleEvents.onNext(LifecycleEvents.DESTROY);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        lifecycleEvents.onNext(LifecycleEvents.SAVEINSTANCESTATE);
    }
}
