package com.JonesRandom.LoginSQLite.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.JonesRandom.LoginSQLite.R;
import com.JonesRandom.LoginSQLite.common.PreferenceManager;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        new Handler().postDelayed(() -> {

            if (PreferenceManager.isUserlogged()) {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashScreenActivity.this, AuthActivity.class));
            }
        }, 3000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
