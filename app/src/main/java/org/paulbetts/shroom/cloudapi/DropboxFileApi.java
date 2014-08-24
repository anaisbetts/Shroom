package org.paulbetts.shroom.cloudapi;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

import org.paulbetts.shroom.R;
import org.paulbetts.shroom.core.Lifecycle;
import org.paulbetts.shroom.core.LifecycleEvents;
import org.paulbetts.shroom.core.RxDaggerActivity;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by paul on 8/14/14.
 */
public class DropboxFileApi implements CloudFileApi {
    private String token;
    private DropboxAPI<AndroidAuthSession> dropboxApi;
    private AppKeyPair keyPair = null;

    @Inject
    public DropboxFileApi() {
    }

    @Inject
    RxDaggerActivity hostActivity;

    @Override
    public String getToken() { return token; }

    @Override
    public void setToken(String value) {
        token = value;
        initializeDropboxApi();
    }

    @Override
    public Observable<String> authenticate() {
        return Observable.create(new Observable.OnSubscribeFunc<String>() {
            @Override
            public Subscription onSubscribe(Observer<? super String> op) {
                initializeDropboxApi();

                // XXX: You can't do this because this subscription gets trashed
                // if the Activity gets torn down
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

                try {
                    dropboxApi.getSession().startOAuth2Authentication(hostActivity);
                } catch (Exception ex) {
                    op.onError(ex);
                }
                return ret;
            }
        });
    }

    @Override
    public Observable<Boolean> testToken() {
        if (dropboxApi == null) initializeDropboxApi();

        return Observable.create(new Observable.OnSubscribeFunc<Boolean>() {
            @Override
            public Subscription onSubscribe(Observer<? super Boolean> op) {
                AsyncTask t = new AsyncTask() {
                    @Override
                    protected Void doInBackground(Object[] objects) {
                        try {
                            DropboxAPI.Account ac = dropboxApi.accountInfo();
                            Log.i("DropboxFileApi", ac.displayName);

                            op.onNext(true);
                            op.onCompleted();
                        } catch (DropboxException de) {
                            op.onNext(false);
                            op.onCompleted();
                        }

                        return null;
                    }
                };

                t.execute();
                return Subscriptions.create(() -> t.cancel(false));
            }
        });
    }

    private void initializeDropboxApi() {
        keyPair = keyPair != null ? keyPair : new AppKeyPair(
                hostActivity.getString(R.string.dropbox_key),
                hostActivity.getString(R.string.dropbox_secret));

        AndroidAuthSession session = new AndroidAuthSession(keyPair);
        dropboxApi = new DropboxAPI(session);

        if (token != null) {
            dropboxApi.getSession().setOAuth2AccessToken(token);
        }
    }

}
