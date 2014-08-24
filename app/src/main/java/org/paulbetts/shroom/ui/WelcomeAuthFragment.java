package org.paulbetts.shroom.ui;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.paulbetts.shroom.R;
import org.paulbetts.shroom.cloudapi.CloudFileApi;
import org.paulbetts.shroom.core.OAuthTokenMixin;
import org.paulbetts.shroom.core.RxDaggerActivity;
import org.paulbetts.shroom.core.RxDaggerFragment;
import org.paulbetts.shroom.models.RomInfo;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.android.observables.ViewObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

public class WelcomeAuthFragment extends RxDaggerFragment {
    @InjectView(R.id.auth_dropbox) Button authDropbox = null;
    @InjectView(R.id.search_scene) LinearLayout searchScene = null;
    @InjectView(R.id.found_rom) TextView foundRom = null;
    @InjectView(R.id.finish_login) Button finishLogin = null;

    @Inject
    RxDaggerActivity hostActivity;

    @Inject
    OAuthTokenMixin oAuthTokenMixin;

    @Inject
    CloudFileApi dropboxApi;

    public WelcomeAuthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View ret = inflater.inflate(R.layout.fragment_welcome_auth, container, false);
        ButterKnife.inject(this, ret);

        Observable<RomInfo> roms = ViewObservable.clicks(authDropbox, false).take(1)
                .doOnNext(x -> oAuthTokenMixin.forgetExistingToken())
                .flatMap(x -> oAuthTokenMixin.initializeHelper(this))
                .doOnNext(x -> Log.i("Shroom", "Logged into Cloud File Backend successfully"))
                .flatMap(x -> {
                    TransitionManager.beginDelayedTransition(container);
                    authDropbox.setVisibility(View.GONE);
                    searchScene.setVisibility(View.VISIBLE);

                    return dropboxApi.scanForRoms();
                })
                .doOnNext(x -> {
                    Log.i("WelcomeActivity", x.getTitle());
                })
                .publish()
                .refCount();

        roms
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(x -> {
                    foundRom.setText("Found " + x.getTitle() + "...");
                })
                .reduce(0, (acc,x) -> acc+1)
                .doOnNext(x -> {
                    TransitionManager.beginDelayedTransition(container);
                    foundRom.setText("Found " + x.toString() + " ROMs");
                    finishLogin.setEnabled(true);
                })
                .flatMap(x -> {
                    return ViewObservable.clicks(finishLogin, false).take(1)
                            .map(y -> true);
                })
                .multicast(loginComplete)
                .connect();

        return ret;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private PublishSubject<Boolean> loginComplete = PublishSubject.create();
    public Observable<Boolean> getLoginComplete() {
        return loginComplete;
    }
}
