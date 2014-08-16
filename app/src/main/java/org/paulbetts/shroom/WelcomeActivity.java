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
    }
}
