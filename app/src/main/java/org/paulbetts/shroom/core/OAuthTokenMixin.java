package org.paulbetts.shroom.core;

import org.paulbetts.shroom.cloudapi.CloudFileApi;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by paul on 8/9/14.
 */
public class OAuthTokenMixin  implements ElementMixin {
    private final String TOKEN_SETTINGS_FILE = "tokens";
    private final String TOKEN_KEY = "fileapi_token";

    @Inject
    CloudFileApi dropboxApi;

    @Inject
    RxDaggerActivity hostActivity;

    @Inject
    public OAuthTokenMixin() {
    }

    @Override
    public Observable<Boolean> initializeHelper(RxDaggerElement activity) {
        return loadAndVerifyToken()
                .map(token -> {
                    hostActivity.getSharedPreferences(TOKEN_SETTINGS_FILE, 0).edit()
                            .putString(TOKEN_KEY, token)
                            .commit();
                    return true;
                });
    }

    public void forgetExistingToken() {
        hostActivity.getSharedPreferences(TOKEN_SETTINGS_FILE, 0).edit()
                .putString(TOKEN_KEY, null)
                .commit();
    }

    private Observable<String> loadAndVerifyToken() {
        Observable<String> getNewKey = dropboxApi.authenticate();

        String oauthToken = hostActivity.getSharedPreferences(TOKEN_SETTINGS_FILE, 0)
                .getString(TOKEN_KEY, null);
        if (oauthToken == null) return getNewKey;

        dropboxApi.setToken(oauthToken);
        return dropboxApi.testToken()
                .map(x -> oauthToken)
                .onErrorResumeNext(getNewKey);
    }
}
