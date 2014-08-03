package org.paulbetts.shroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.javatuples.Triplet;
import org.paulbetts.shroom.core.ActivityHelper;
import org.paulbetts.shroom.core.DaggerApplication;

import rx.Observable;
import rx.functions.Func0;
import rx.subjects.PublishSubject;
import rx.*;

/**
 * Created by paul on 8/1/14.
 */
public class RxDaggerActivity extends Activity {
    PublishSubject<LifecycleEvents> lifecycleEvents = PublishSubject.create();

    public Observable<LifecycleEvents> getLifecycleEvents() {
        return lifecycleEvents;
    }

    PublishSubject<Triplet<Integer, Integer, Intent>> activityResult = PublishSubject.create();

    public Observable<Triplet<Integer, Integer, Intent>> getActivityResult() {
        return activityResult;
    }

    static int nextRequest = 0x10000;
    public Observable<Triplet<Integer, Integer, Intent>> startObsActivityForResult(Intent intent) {
        int current = nextRequest++;

        this.startActivityForResult(intent, current);
        return this.getActivityResult().filter(x -> x.getValue0() == current);
    }

    public Observable<Boolean> applyActivityHelpers(ActivityHelper... helpers){
        // NB: Compiler isn't clever enough to infer type :(
        return Observable.from(helpers)
                .concatMap(x -> Observable.defer(new Func0<Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call() {
                        return x.initializeHelper(RxDaggerActivity.this);
                    }
                }))
                .reduce((Boolean) true, (acc, x) -> acc && x);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activityResult.onNext(Triplet.with(requestCode, resultCode, data));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DaggerApplication)getApplication()).inject(this);

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
