package org.paulbetts.shroom.core;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created by paul on 8/12/14.
 */
public class Lifecycle {

    public static Observable<LifecycleEvents> getLifecycleFor(RxDaggerElement element, LifecycleEvents... events) {
        return element.getLifecycleEvents().filter(x -> {
            for(LifecycleEvents ev: events) {
                if (x == ev) return true;
            }

            return false;
        });
    }

    public static Observable<Boolean> applyActivityHelpers(final RxDaggerElement element, ElementMixin... helpers){
        // NB: Compiler isn't clever enough to infer type :(
        return getLifecycleFor(element, LifecycleEvents.CREATE)
                .take(1)
                .flatMap(x -> Observable.from(helpers))
                .concatMap(x -> Observable.defer(new Func0<Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call() {
                        return x.initializeHelper(element);
                    }
                }))
                .reduce((Boolean) true, (acc, x) -> acc && x);
    }
}
