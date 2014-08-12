package org.paulbetts.shroom.core;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Observable;
import rx.subjects.ReplaySubject;

/**
 * Created by paul on 8/12/14.
 */
public abstract class RxDaggerFragment extends Fragment {
    private ReplaySubject<LifecycleEvents> lifecycleEvents = ReplaySubject.create(1);
    private Bundle currentBundle = null;
    private Activity currentActivity = null;

    public Observable<LifecycleEvents> getLifecycleEvents() {
        return lifecycleEvents;
    }

    public Bundle getCurrentBundle() {
        return currentBundle;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        currentActivity = activity;
        lifecycleEvents.onNext(LifecycleEvents.ATTACH);
        currentActivity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentBundle = savedInstanceState;
        lifecycleEvents.onNext(LifecycleEvents.CREATE);
        currentBundle = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        currentBundle = savedInstanceState;
        lifecycleEvents.onNext(LifecycleEvents.ACTIVITYCREATED);
        currentBundle = null;
    }


    @Override
    public void onStart() {
        super.onStart();
        lifecycleEvents.onNext(LifecycleEvents.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleEvents.onNext(LifecycleEvents.RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        lifecycleEvents.onNext(LifecycleEvents.PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycleEvents.onNext(LifecycleEvents.STOP);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleEvents.onNext(LifecycleEvents.DESTROY);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lifecycleEvents.onNext(LifecycleEvents.DETACH);
    }
}
