package org.paulbetts.shroom.cloudapi;

import rx.Observable;

/**
 * Created by paul on 8/14/14.
 */

public interface CloudFileApi {
    Observable<String> authenticate();
    Observable<Void> testToken(String token);
}
