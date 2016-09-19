package com.JonesRandom.LoginSQLite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashScreenAbalAbal extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getSharedPreferences("login_session",MODE_PRIVATE);
        boolean status = pref.getBoolean("status_login",false);
        if (status) {
            startActivity(new Intent(SplashScreenAbalAbal.this,User.class));
            finish();
        } else {
            startActivity(new Intent(SplashScreenAbalAbal.this, MainActivity.class));
            finish();
        }

    }
}
