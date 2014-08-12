package org.paulbetts.shroom.core;

import android.os.Bundle;

import rx.Observable;

/**
 * Created by paul on 8/12/14.
 */
public interface RxDaggerElement {
    Observable<LifecycleEvents> getLifecycleEvents();
    Bundle getCurrentBundle();

    void inject(Object target);
}
