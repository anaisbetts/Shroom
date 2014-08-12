package org.paulbetts.shroom;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.paulbetts.shroom.core.Lifecycle;
import org.paulbetts.shroom.core.OAuthTokenMixin;
import org.paulbetts.shroom.core.RxDaggerActivity;
import org.paulbetts.shroom.core.RxDaggerFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.android.observables.ViewObservable;

public class WelcomeAuthFragment extends RxDaggerFragment {
    @InjectView(R.id.auth_gdrive) Button authGDrive = null;

    @Inject
    RxDaggerActivity hostActivity;

    @Inject
    OAuthTokenMixin oAuthTokenMixin;

    public WelcomeAuthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.fragment_welcome_auth, container, false);
        ButterKnife.inject(this, ret);

        oAuthTokenMixin.forgetExistingToken();
        ViewObservable.clicks(authGDrive, false)
                .flatMap(x -> oAuthTokenMixin.initializeHelper(this).onErrorResumeNext(Observable.never()))
                .subscribe(x -> Log.i("Shroom", "Logged into GDrive successfully"));

        return ret;
    }
}
