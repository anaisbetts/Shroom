package org.paulbetts.shroom.core;

import rx.Observable;

public interface ActivityHelper {
    Observable<Boolean> initializeHelper(RxDaggerActivity activity);
}
