package org.paulbetts.shroom;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.paulbetts.shroom.core.OAuthTokenMixin;
import org.paulbetts.shroom.core.RxDaggerActivity;
import org.paulbetts.shroom.core.RxDaggerFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.android.observables.ViewObservable;
import rx.subjects.PublishSubject;

public class WelcomeAuthFragment extends RxDaggerFragment {
    @InjectView(R.id.auth_dropbox) Button authDropbox = null;

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

        ViewObservable.clicks(authDropbox, false)
                .doOnNext(x -> oAuthTokenMixin.forgetExistingToken())
                .flatMap(x -> oAuthTokenMixin.initializeHelper(this))
                .doOnNext(x -> Log.i("Shroom", "Logged into Cloud File Backend successfully"))
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
