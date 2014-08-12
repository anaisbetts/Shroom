package org.paulbetts.shroom.core;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import org.javatuples.Pair;
import org.paulbetts.shroom.helpers.GDriveService;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.subjects.AsyncSubject;

/**
 * Created by paul on 8/9/14.
 */
public class OAuthTokenMixin implements ElementMixin {
    private static final String OAUTH_SCOPES = "oauth2:https://www.googleapis.com/auth/drive";
    private String oauthToken;

    public GDriveService driveService;

    @Inject
    RxDaggerActivity hostActivity;

    @Inject
    public OAuthTokenMixin() {
    }

    @Override
    public Observable<Boolean> initializeHelper(RxDaggerElement activity) {
        return loadAndVerifyToken(activity)
                .map(token -> {
                    driveService = createDriveService(token);
                    this.oauthToken = token;

                    hostActivity.getSharedPreferences("tokens", 0).edit()
                            .putString("gdrive", token)
                            .commit();
                    return true;
                });
    }

    public void forgetExistingToken() {
        hostActivity.getSharedPreferences("tokens", 0).edit()
                .putString("gdrive", null)
                .commit();
    }

    private Observable<String> loadAndVerifyToken(RxDaggerElement activity) {
        Observable<String> getNewKey = Observable.defer(new Func0<Observable<? extends String>>() {
            @Override
            public Observable<? extends String> call() {
                return invokeAccountChooser(hostActivity)
                        .flatMap(x -> getTokenForAccount(x, hostActivity));
            }
        });

        String oauthToken = hostActivity.getSharedPreferences("tokens", 0)
                .getString("gdrive", null);
        if (oauthToken == null) return getNewKey;

        GDriveService svc = createDriveService(oauthToken);
        return svc.getUserInfo()
                .map(x -> oauthToken)
                .onErrorResumeNext(getNewKey);
    }

    private GDriveService createDriveService(String oauthToken) {
        RequestInterceptor tokenIntercepter = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Authorization", "Bearer " + oauthToken);
                request.addHeader("X-JavaScript-User-Agent", "Shroom");
                request.addQueryParam("key", oauthToken);
            }
        };

        RestAdapter gdriveAdapter = new RestAdapter.Builder()
                .setEndpoint("https://www.googleapis.com")
                .setRequestInterceptor(tokenIntercepter)
                .build();

        return gdriveAdapter.create(GDriveService.class);
    }

    private Observable<Account> invokeAccountChooser(RxDaggerActivity activity) {
        Intent chooser = AccountManager.newChooseAccountIntent(null, null, new String[] { "com.google" }, false, null, null, null, null);
        AccountManager acMgr = AccountManager.get(activity);

        return activity.startObsActivityForResult(chooser, 0, 0)
                .flatMap(x -> {
                    if (x.getValue0() == Activity.RESULT_CANCELED)
                        return Observable.error(new Exception("Operation Canceled"));

                    return Observable.just(x);
                })
                .flatMap(x -> {
                    String name = x.getValue1().getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    for (Account needle : acMgr.getAccountsByType("com.google")) {
                        if (needle.name.equals(name)) return Observable.just(needle);
                    }

                    return Observable.error(new OperationCanceledException());
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

                    SharedPreferences prefs = activity.getSharedPreferences("Settings", 0);
                    prefs.edit().putString("oauthAccount", acct.name).commit();

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
