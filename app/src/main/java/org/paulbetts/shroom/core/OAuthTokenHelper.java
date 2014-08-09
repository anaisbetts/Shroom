package org.paulbetts.shroom.core;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import org.paulbetts.shroom.helpers.GPlusService;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import rx.Observable;
import rx.functions.Func0;
import rx.subjects.AsyncSubject;

/**
 * Created by paul on 8/9/14.
 */
public class OAuthTokenHelper implements ActivityHelper {
    private String OAUTH_SCOPES = "oauth2:https://www.googleapis.com/auth/drive oauth2:https://www.googleapis.com/auth/userinfo.profile";

    private String oauthToken = null;

    @Override
    public Observable<Boolean> initializeHelper(RxDaggerActivity activity) {
        return activity.getLifecycleFor(LifecycleEvents.CREATE)
                .flatMap(x -> loadAndVerifyToken(activity))
                .map(x -> {
                    oauthToken = x;

                    SharedPreferences prefs = activity.getSharedPreferences("Settings", 0);
                    prefs.edit().putString("authToken", oauthToken).commit();
                    return true;
                });
    }

    private Observable<String> loadAndVerifyToken(RxDaggerActivity activity) {
        Observable<String> getNewKey = Observable.defer(new Func0<Observable<? extends String>>() {
            @Override
            public Observable<? extends String> call() {
                return invokeAccountChooser(activity)
                        .flatMap(x -> getTokenForAccount(x, activity));
            }
        });

        SharedPreferences prefs = activity.getSharedPreferences("Settings", 0);
        oauthToken = prefs.getString("authToken", null);

        if (oauthToken == null) return getNewKey;

        RequestInterceptor tokenIntercepter = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Authorization", "Bearer " + oauthToken);
                request.addHeader("X-JavaScript-User-Agent", "Shroom");
            }
        };

        RestAdapter gplusAdapter = new RestAdapter.Builder()
                .setEndpoint("https://www.googleapis.com")
                .setRequestInterceptor(tokenIntercepter)
                .build();

        GPlusService svc = gplusAdapter.create(GPlusService.class);

        return svc.getProfileInfo()
                .map(x -> oauthToken)
                .onErrorResumeNext(getNewKey);
    }

    private Observable<Account> invokeAccountChooser(RxDaggerActivity activity) {
        Intent chooser = AccountManager.newChooseAccountIntent(null, null, new String[] { "com.google" }, false, null, null, null, null);
        AccountManager acMgr = AccountManager.get(activity);

        return activity.startObsActivityForResult(chooser, 0, 0)
                .flatMap(x -> {
                    if (x.getValue0() == Activity.RESULT_CANCELED) return Observable.error(new Exception("Operation Canceled"));
                    return Observable.just(x);
                })
                .map(x -> {
                    String name = x.getValue1().getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    for(Account needle: acMgr.getAccountsByType("com.google")) {
                        if (needle.name == name) return needle;
                    }

                    return (Account)null;
                });
    }

    private Observable<String> getTokenForAccount(Account acct, RxDaggerActivity activity){
        AccountManager acMgr = AccountManager.get(activity);
        final AsyncSubject<String> ret = AsyncSubject.create();

        acMgr.getAuthToken(acct, OAUTH_SCOPES, null, activity, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> bundleAccountManagerFuture) {
                try {
                    String token = bundleAccountManagerFuture.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                    ret.onNext(token);
                    ret.onCompleted();
                } catch (Exception ex) {
                    ret.onError(ex);
                }
            }
        }, null);

        return ret;
    }
}
