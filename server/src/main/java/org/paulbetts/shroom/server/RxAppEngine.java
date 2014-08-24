package org.paulbetts.shroom.server;

import com.google.appengine.api.ThreadManager;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func0;
import rx.subscriptions.Subscriptions;

/**
 * Created by paul on 8/22/14.
 */
public class RxAppEngine {
    public static <T> Observable<T> deferStart(final Func0<T> action) {
        return Observable.create(new Observable.OnSubscribeFunc<T>() {
            boolean isCancelled = false;

            @Override
            public Subscription onSubscribe(final Observer<? super T> subj) {
                Thread t = ThreadManager.currentRequestThreadFactory().newThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isCancelled == true) return;

                        try {
                            subj.onNext(action.call());
                            subj.onCompleted();
                        } catch (Exception ex) {
                            subj.onError(ex);
                        }
                    }
                });

                if (!isCancelled) t.start();

                return Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        isCancelled = true;
                    }
                });
            }
        });
    }
}
