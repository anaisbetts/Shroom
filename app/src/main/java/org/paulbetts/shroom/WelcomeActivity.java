package org.paulbetts.shroom;

import android.app.FragmentTransaction;
import android.os.Bundle;

import org.paulbetts.shroom.core.AppSettingsMixin;
import org.paulbetts.shroom.core.Lifecycle;
import org.paulbetts.shroom.core.OAuthTokenMixin;
import org.paulbetts.shroom.core.RxDaggerActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class WelcomeActivity extends RxDaggerActivity {
    @Inject
    AppSettingsMixin appSettings;

    @Inject
    OAuthTokenMixin oauthTokens;

    @Inject
    CategoryScanners scanners;

    @Inject
    WelcomeAuthFragment authFragment;

    @Inject
    DriveFolderSelectorFragment driveSelectorFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.inject(this);

        this.setResult(RESULT_CANCELED);

        Lifecycle.applyActivityHelpers(this, appSettings).subscribe(applyWorked -> {
            authFragment.getLoginComplete().subscribe(x -> {
                setResult(RESULT_OK);
                finish();
            });

            getFragmentManager().beginTransaction()
                    .replace(R.id.welcome_content, authFragment)
                    .commit();
        });

            /*
            authFragment.getLoginComplete().subscribe(x -> {
                getFragmentManager().beginTransaction()
                        .replace(R.id.welcome_content, driveSelectorFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit();
            });
                        */

            /*
            chooseFolder.setOnClickListener(view -> {
                oauthTokens.driveService.list("title contains 'smc'")
                        .subscribe(x -> {
                            for (DriveItem di: x.getItems()) {
                                Log.i("WelcomeActivity", di.getTitle());
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
            });
                        */
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
