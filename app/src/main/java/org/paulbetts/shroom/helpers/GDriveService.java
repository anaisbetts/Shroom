package org.paulbetts.shroom.helpers;

import org.paulbetts.shroom.gdrive.FileList;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by paul on 8/9/14.
 */
public interface GDriveService {

    @GET("/drive/v2/files?maxResults=1000")
    Observable<FileList> list(@Query("q") String query);

    @GET("/drive/v2/files?maxResults=1000")
    Observable<FileList> listNextPage(@Query("pageToken") String nextPageToken);
}
