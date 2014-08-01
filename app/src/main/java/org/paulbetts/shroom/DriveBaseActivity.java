package org.paulbetts.shroom;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import rx.Observable;
import rx.subjects.AsyncSubject;

public abstract class DriveBaseActivity extends RxActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    protected GoogleApiClient googleApiClient;
    private boolean isAuthorizing = false;

    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 9395;

    private AsyncSubject<Bundle> connectedToDrive = AsyncSubject.create();

    public Observable<Bundle> getConnectedToDrive() {
        return connectedToDrive;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        googleApiClient = new GoogleApiClient.Builder(this)
            .addApi(Drive.API)
            .addScope(Drive.SCOPE_FILE)
            .addScope(Drive.SCOPE_APPFOLDER)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isAuthorizing) googleApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESOLVE_CONNECTION_REQUEST_CODE && resultCode == RESULT_OK) {
            isAuthorizing = false;
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        connectedToDrive.onNext(bundle);
        connectedToDrive.onCompleted();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                isAuthorizing = true;
                connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
            connectedToDrive.onError(new Exception("Failed to connect to Drive"));
        }
    }
}
