package org.paulbetts.shroom.core;

import android.os.Bundle;
import android.view.View;

import rx.Observable;

/**
 * Created by paul on 8/12/14.
 */
public interface RxDaggerElement {
    Observable<LifecycleEvents> getLifecycleEvents();
    Bundle getCurrentBundle();
    View getRootView();

    void inject(Object target);
}
