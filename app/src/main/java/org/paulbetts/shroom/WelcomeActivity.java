package org.paulbetts.shroom;

import android.os.Bundle;
import org.paulbetts.shroom.R;

public class WelcomeActivity extends RxDaggerActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
}
