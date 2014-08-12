package org.paulbetts.shroom.helpers;

import org.paulbetts.shroom.gdrive.DriveItem;
import org.paulbetts.shroom.gdrive.FileList;
import org.paulbetts.shroom.gdrive.UserInfo;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by paul on 8/9/14.
 */
public interface GDriveService {
    @GET("/drive/v2/files/{folderId}/children?maxResults=100")
    Observable<FileList> listChildren(@Path("folderId") String folderId);

    @GET("/drive/v2/files/root/children?maxResults=100")
    Observable<FileList> listRoot();

    @GET("/drive/v2/files?maxResults=100")
    Observable<FileList> search(@Query("q") String query);

    @GET("/drive/v2/files/{id}")
    Observable<DriveItem> getItem(@Path("id") String id);

    @GET("/drive/v2/files?maxResults=100")
    Observable<FileList> getNextPage(@Query("pageToken") String nextPageToken);

    @GET("/drive/v2/about")
    Observable<UserInfo> getUserInfo();
}
