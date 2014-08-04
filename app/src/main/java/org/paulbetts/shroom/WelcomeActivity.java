package org.paulbetts.shroom;

import android.app.Activity;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;

public class WelcomeActivity extends DriveBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        this.setResult(RESULT_CANCELED);

        Button chooseFolder = (Button)findViewById(R.id.chooseFolderInDrive);
        chooseFolder.setClickable(false);

        getConnectedToDrive().subscribe(conn -> chooseFolder.setClickable(true));

        chooseFolder.setOnClickListener(view -> {
            IntentSender sender = Drive.DriveApi
                    .newOpenFileActivityBuilder()
                    .setMimeType(new String[] {DriveFolder.MIME_TYPE })
                    .build(googleApiClient);

            this.startObsIntentSenderForResult(sender)
                    .filter(x -> x.getValue0() == Activity.RESULT_OK)
                    .subscribe(x -> {
                        DriveId driveId = x.getValue1().getParcelableExtra(
                                OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                        Log.i("WelcomeActivity", "Selected folder's ID: " + driveId);

                        WelcomeActivity.this.setResult(RESULT_OK);
                        WelcomeActivity.this.finish();
                    });
        });
    }
}
