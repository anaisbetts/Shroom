package org.paulbetts.shroom.core;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Observable;
import rx.functions.Func3;
import rx.subjects.ReplaySubject;

/**
 * Created by paul on 8/12/14.
 */
public class RxDaggerListFragment extends ListFragment  {
    private ReplaySubject<LifecycleEvents> lifecycleEvents = ReplaySubject.create(1);
    private Bundle currentBundle = null;
    private Activity currentActivity = null;
    private View currentView = null;

    private Func3<LayoutInflater, ViewGroup, Bundle, View> createViewOverride = null;

    public Observable<LifecycleEvents> getLifecycleEvents() {
        return lifecycleEvents;
    }

    public Bundle getCurrentBundle() {
        return currentBundle;
    }

    public Activity getCurrentActivity() { return currentActivity; }

    public View getRootView(){
        return currentView;
    }

    public void setCreateViewOverride(Func3<LayoutInflater, ViewGroup, Bundle, View> createViewOverride) {
        this.createViewOverride = createViewOverride;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (createViewOverride != null) {
            return createViewOverride.call(inflater, container, savedInstanceState);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentView = view;
        currentBundle = savedInstanceState;
        lifecycleEvents.onNext(LifecycleEvents.VIEWCREATED);
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
        lifecycleEvents.onNext(LifecycleEvents.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        lifecycleEvents.onNext(LifecycleEvents.STOP);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        lifecycleEvents.onNext(LifecycleEvents.DESTROYVIEW);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        lifecycleEvents.onNext(LifecycleEvents.DESTROY);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        lifecycleEvents.onNext(LifecycleEvents.DETACH);
        super.onDetach();
    }
}
