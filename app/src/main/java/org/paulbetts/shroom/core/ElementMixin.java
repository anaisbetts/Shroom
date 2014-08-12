package org.paulbetts.shroom.core;

import rx.Observable;

public interface ElementMixin {
    Observable<Boolean> initializeHelper(RxDaggerElement host);
}
