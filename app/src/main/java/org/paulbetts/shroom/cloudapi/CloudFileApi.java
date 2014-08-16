package org.paulbetts.shroom.cloudapi;

import rx.Observable;

/**
 * Created by paul on 8/14/14.
 */

public interface CloudFileApi {
    String getToken();
    void setToken(String value);

    Observable<String> authenticate();
    Observable<Boolean> testToken(String token);
}
