package org.paulbetts.shroom.cloudapi;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.paulbetts.shroom.R;
import org.paulbetts.shroom.core.Lifecycle;
import org.paulbetts.shroom.core.LifecycleEvents;
import org.paulbetts.shroom.core.RxDaggerActivity;
import org.paulbetts.shroom.core.RxOkHttp;
import org.paulbetts.shroom.models.RomInfo;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by paul on 8/14/14.
 */
public class DropboxServerAssistedFileApi implements CloudFileApi {
    private String token;
    private DropboxAPI<AndroidAuthSession> dropboxApi;
    private AppKeyPair keyPair = null;
    private String scannerServiceHost = null;
    private OkHttpClient client = new OkHttpClient();

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
        return Observable.create((Subscriber<? super String> op) -> {
            initializeDropboxApi();

            // XXX: You can't do this because this subscription gets trashed
            // if the Activity gets torn down
            op.add(Lifecycle.getLifecycleFor(hostActivity, LifecycleEvents.RESUME)
                    .skip(1).take(1)
                    .flatMap(x -> {
                        if (dropboxApi.getSession().authenticationSuccessful()) {
                            // Required to complete auth, sets the access token on the session
                            dropboxApi.getSession().finishAuthentication();

                            return Observable.just(token = dropboxApi.getSession().getOAuth2AccessToken());
                        } else {
                            return Observable.error(new Exception("Authentication failed"));
                        }
                    })
                    .subscribe(op));

            try {
                dropboxApi.getSession().startOAuth2Authentication(hostActivity);
            } catch (Exception ex) {
                op.onError(ex);
            }
        });
    }

    @Override
    public Observable<Boolean> testToken() {
        if (dropboxApi == null) initializeDropboxApi();

        return Observable.create((Subscriber<? super Boolean> op) -> {
            AsyncTask t = new AsyncTask() {
                @Override
                protected Void doInBackground(Object[] objects) {
                    try {
                        if (op.isUnsubscribed()) return null;

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

            op.add(Subscriptions.create(() -> t.cancel(false)));

            t.execute();
        });
    }

    @Override
    public Observable<RomInfo> scanForRoms() {
        scannerServiceHost = scannerServiceHost != null ?
                scannerServiceHost :
                hostActivity.getString(R.string.appengine_scanner_service);

        Request rq = new Request.Builder()
                .url(scannerServiceHost + "/scanner")
                .get()
                .addHeader("Authorization", "Bearer " + getToken())
                .build();

        return RxOkHttp.streamLines(client, rq)
                .map(x -> new Gson().fromJson(x, RomInfo.class));
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
