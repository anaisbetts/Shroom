package org.paulbetts.shroom.core;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.List;

import dagger.Module;
import dagger.ObjectGraph;
import rx.Observable;
import rx.functions.Func0;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

/**
 * Created by paul on 8/1/14.
 */
public abstract class RxDaggerActivity extends Activity implements RxDaggerElement {
    private ReplaySubject<LifecycleEvents> lifecycleEvents = ReplaySubject.create(1);
    private Bundle currentBundle = null;
    private PublishSubject<Triplet<Integer, Integer, Intent>> activityResult = PublishSubject.create();
    private ObjectGraph activityGraph;

    public Observable<LifecycleEvents> getLifecycleEvents() {
        return lifecycleEvents;
    }
    public Bundle getCurrentBundle() {
        return currentBundle;
    }

    public Observable<Triplet<Integer, Integer, Intent>> getActivityResult() {
        return activityResult;
    }

    static int nextRequest = 0x10000;
    public Observable<Pair<Integer, Intent>> startObsActivityForResult(Intent intent, int enterAnim, int exitAnim) {
        int current = nextRequest++;

        this.startActivityForResult(intent, current);
        this.overridePendingTransition(enterAnim, exitAnim);

        return this.getActivityResult()
                .filter(x -> x.getValue0() == current)
                .map(x -> Pair.with(x.getValue1(), x.getValue2()))
                .take(1)
                .publishLast().refCount();
    }

    public Observable<Pair<Integer, Intent>> startObsIntentSenderForResult(IntentSender intent, int enterAnim, int exitAnim) {
        int current = nextRequest++;

        try {
            this.startIntentSenderForResult(intent, current, null, 0, 0, 0);
            this.overridePendingTransition(enterAnim, exitAnim);
        } catch (IntentSender.SendIntentException e) {
            return Observable.error(e);
        }

        return this.getActivityResult()
                .filter(x -> x.getValue0() == current)
                .map(x -> Pair.with(x.getValue1(), x.getValue2()))
                .take(1)
                .publishLast().refCount();
    }

    public void inject(Object target) {
        activityGraph.inject(target);
    }

    protected List<Module> getModules() {
        return null;
    }

    public View getRootView() {
        return this.findViewById(android.R.id.content);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activityResult.onNext(Triplet.with(requestCode, resultCode, data));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityGraph = ((DaggerApplication)getApplication()).getApplicationGraph()
                .plus(new ActivityModule(this));
        List<Module> modules = getModules();
        if (modules != null) {
            activityGraph = activityGraph.plus(modules.toArray());
        }

        inject(this);

        currentBundle = savedInstanceState;
        lifecycleEvents.onNext(LifecycleEvents.CREATE);
        currentBundle = null;
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
        lifecycleEvents.onNext(LifecycleEvents.PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifecycleEvents.onNext(LifecycleEvents.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        lifecycleEvents.onNext(LifecycleEvents.DESTROY);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        currentBundle = outState;
        lifecycleEvents.onNext(LifecycleEvents.SAVEINSTANCESTATE);
        currentBundle = null;
    }
}
