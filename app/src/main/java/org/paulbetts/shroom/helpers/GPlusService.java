package org.paulbetts.shroom.helpers;

import org.paulbetts.shroom.gplus.ProfileInfo;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by paul on 8/9/14.
 */
public interface GPlusService {
    @GET("/plus/v1/people/me")
    public Observable<ProfileInfo> getProfileInfo();
}
