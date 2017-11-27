package com.JonesRandom.LoginSQLite;

import android.app.Application;

import com.JonesRandom.LoginSQLite.common.PreferenceManager;
import com.JonesRandom.LoginSQLite.database.DatabaseHelper;

/**
 * Created by jonesrandom on 11/27/17.
 * <p>
 * AA
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseHelper.initDatabaseHelper(this);
        PreferenceManager.initPreferences(this);

    }
}
