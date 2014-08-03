package org.paulbetts.shroom;

import android.os.Bundle;
import android.widget.Button;

public class WelcomeActivity extends DriveBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        this.setResult(RESULT_CANCELED);

        /*
        Button doIt = (Button)findViewById(R.id.doIt);
        doIt.setOnClickListener(view -> {
            WelcomeActivity.this.setResult(RESULT_OK);
            WelcomeActivity.this.finish();
        });
        */
    }
}
