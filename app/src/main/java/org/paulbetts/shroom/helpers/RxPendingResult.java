package org.paulbetts.shroom.helpers;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by paul on 8/6/14.
 */
public class RxPendingResult<T extends Result> {
    public static <T extends Result> Observable<T> from(PendingResult<T> result)  {
        return Observable.create(new Observable.OnSubscribeFunc<T>() {
            @Override
            public Subscription onSubscribe(Observer<? super T> subj) {
                result.setResultCallback(result -> {
                    Status s = result.getStatus();

                    if (!s.isSuccess()) {
                        subj.onError(new StatusException(s));
                        return;
                    }

                    subj.onNext(result);
                    subj.onCompleted();
                });

                return Subscriptions.create(() -> result.cancel());
            }
        });
    }
}
