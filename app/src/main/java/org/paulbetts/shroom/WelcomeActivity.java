package org.paulbetts.shroom;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.drive.query.Query;

import org.javatuples.Pair;
import org.paulbetts.shroom.core.AppSettingsHelper;
import org.paulbetts.shroom.core.OAuthTokenHelper;
import org.paulbetts.shroom.core.RxDaggerActivity;
import org.paulbetts.shroom.helpers.RxPendingResult;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func2;

public class WelcomeActivity extends RxDaggerActivity {
    @Inject
    AppSettingsHelper appSettings;

    @Inject
    OAuthTokenHelper oauthTokens;

    @Inject
    CategoryScanners scanners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        this.setResult(RESULT_CANCELED);

        Button chooseFolder = (Button) findViewById(R.id.chooseFolderInDrive);
        chooseFolder.setClickable(false);

        applyActivityHelpers(appSettings, oauthTokens).subscribe(applyWorked -> {
            chooseFolder.setOnClickListener(view -> {
                /*
                IntentSender sender = Drive.DriveApi
                        .newOpenFileActivityBuilder()
                        .setMimeType(new String[]{DriveFolder.MIME_TYPE})
                        .build(googleApiClient);
                        */

                /*
                this.startObsIntentSenderForResult(sender, android.R.anim.fade_in, android.R.anim.fade_out)
                        .filter(x -> x.getValue0() == Activity.RESULT_OK)
                        .observeOn(Schedulers.io())
                        .map(x -> getFolderFromResultAndSave(x.getValue1()))
                        .map(x -> x.listChildren(googleApiClient).await())
                        .subscribe(x -> {
                            MetadataBuffer buf = x.getMetadataBuffer();
                            int count = buf.getCount();
                            for (int i = 0; i < count; i++) {
                                String dbg = "Found a ROM: " + buf.get(i).getTitle() + ", " + buf.get(i).getMimeType();
                                Log.i("WelcomeActivity", dbg);
                            }
                        });
                        */

                /*
                this.startObsIntentSenderForResult(sender, android.R.anim.fade_in, android.R.anim.fade_out)
                        .filter(x -> x.getValue0() == Activity.RESULT_OK)
                        .map(x -> getFolderFromResultAndSave(x.getValue1()))
                        .flatMap(x -> scanForRoms(x))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(x -> {
                            for(PlayableRom pr: x) {
                                Log.i("WelcomeActivity", "Found a ROM: " + pr.getFriendlyTitle());
                            }

                            WelcomeActivity.this.setResult(RESULT_OK);
                            WelcomeActivity.this.finish();
                        });
                        */
            });
        });
    }

    /*
    DriveFolder getFolderFromResultAndSave(Intent intentResult) {
        DriveId driveId = intentResult.getParcelableExtra(
                OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

        DriveFolder folder = Drive.DriveApi.getFolder(googleApiClient, driveId);
        appSettings.setRootRomFolder(folder);

        Log.i("WelcomeActivity", "Selected folder's ID: " + driveId);
        //Log.i("WelcomeActivity", "Selected Folder Name:" + folder.getMetadata(googleApiClient).await().getMetadata().getTitle());
        return folder;
    }

    Observable<Iterable<PlayableRom>> scanForRoms(DriveFolder driveFolder) {
        Observable<Observable<Pair<CategoryScanner, MetadataBuffer>>> scans =
                Observable.from(scanners.get())
                        .map(scanner -> makeQueryRx(driveFolder, scanner.generateQueryForCategory())
                                .map(result -> Pair.with(scanner, result)));

        Observable<ArrayList<PlayableRom>> roms = Observable.merge(scans, 2)
                .reduce(new ArrayList<PlayableRom>(), new Func2<ArrayList<PlayableRom>, Pair<CategoryScanner, MetadataBuffer>, ArrayList<PlayableRom>>() {
                    @Override
                    public ArrayList<PlayableRom> call(ArrayList<PlayableRom> acc, Pair<CategoryScanner, MetadataBuffer> x) {
                        CategoryScanner scanner = x.getValue0();
                        MetadataBuffer buf = x.getValue1();

                        int count = buf.getCount();
                        for (int i=0; i < count; i++) {
                            acc.add(scanner.romForDriveFile(buf.get(i)));
                        }

                        return acc;
                    }
                });

        return roms.map(x -> (Iterable<PlayableRom>)x);
    }

    Observable<MetadataBuffer> makeQueryRx(DriveFolder folder, Query query) {
        return Observable.defer(new Func0<Observable<MetadataBuffer>>() {
            @Override
            public Observable<MetadataBuffer> call() {
                //return RxPendingResult.from(folder.listChildren(googleApiClient))
                return RxPendingResult.from(Drive.DriveApi.query(googleApiClient, query))
                    .map(x -> x.getMetadataBuffer());
            };
        });
    }
    */
}
