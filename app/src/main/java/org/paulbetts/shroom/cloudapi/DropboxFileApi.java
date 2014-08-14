package org.paulbetts.shroom.cloudapi;

import rx.Observable;

/**
 * Created by paul on 8/14/14.
 */
public class DropboxFileApi implements CloudFileApi {
    private String token;

    @Override
    public String getToken() { return token; }

    @Override
    public void setToken(String value) { token = value; }

    @Override
    public Observable<String> authenticate() {
        return null;
    }

    @Override
    public Observable<Void> testToken(String token) {
        return null;
    }
}
