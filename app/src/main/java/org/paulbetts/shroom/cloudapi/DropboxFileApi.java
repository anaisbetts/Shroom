package org.paulbetts.shroom.cloudapi;

import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

import org.paulbetts.shroom.core.Lifecycle;
import org.paulbetts.shroom.core.LifecycleEvents;
import org.paulbetts.shroom.core.RxDaggerActivity;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by paul on 8/14/14.
 */
public class DropboxFileApi implements CloudFileApi {
    private String token;
    private DropboxAPI<AndroidAuthSession> dropboxApi;

    @Inject
    RxDaggerActivity hostActivity;

    @Override
    public String getToken() { return token; }

    @Override
    public void setToken(String value) { token = value; }

    @Override
    public Observable<String> authenticate() {
        return Observable.create(new Observable.OnSubscribeFunc<String>() {
            @Override
            public Subscription onSubscribe(Observer<? super String> op) {
                AppKeyPair pair = new AppKeyPair("ou3actdv23d43xx", "hsytki045vo14ta");
                AndroidAuthSession session = new AndroidAuthSession(pair, Session.AccessType.AUTO);

                dropboxApi = new DropboxAPI<AndroidAuthSession>(session);
                Subscription ret = Lifecycle.getLifecycleFor(hostActivity, LifecycleEvents.RESUME)
                        .skip(1).take(1)
                        .flatMap(x -> {
                            if (dropboxApi.getSession().authenticationSuccessful()) {
                                // Required to complete auth, sets the access token on the session
                                dropboxApi.getSession().finishAuthentication();

                                return Observable.just(dropboxApi.getSession().getOAuth2AccessToken());
                            } else {
                                return Observable.error(new Exception("Authentication failed"));
                            }
                        })
                        .subscribe(op);

                dropboxApi.getSession().startOAuth2Authentication(hostActivity);
                return ret;
            }
        });
    }

    @Override
    public Observable<Boolean> testToken(String token) {
        return dropboxApi.getSession().authenticationSuccessful() ?
                Observable.just(true) :
                Observable.error(new Exception("Token is not valid"));
    }
}
