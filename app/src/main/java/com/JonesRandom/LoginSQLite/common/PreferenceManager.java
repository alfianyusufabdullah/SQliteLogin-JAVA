package com.JonesRandom.LoginSQLite.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.JonesRandom.LoginSQLite.database.DatabaseConstan;
import com.JonesRandom.LoginSQLite.model.ModelUser;

/**
 * Created by jonesrandom on 11/27/17.
 * <p>
 * AA
 */

public class PreferenceManager {

    private static SharedPreferences preferences;
    private static String PREF_LOGIN = "login";

    private PreferenceManager(Context context) {
        preferences = context.getSharedPreferences("PREF_MANAGER", Context.MODE_PRIVATE);
    }

    public static PreferenceManager initPreferences(Context context) {
        return new PreferenceManager(context);
    }

    public static boolean isUserlogged() {
        return preferences.getBoolean(PREF_LOGIN, false);
    }

    public static ModelUser getUser() {

        ModelUser user = new ModelUser();
        user.userID = preferences.getInt(DatabaseConstan.ROW_ID, 0);
        user.user = preferences.getString(DatabaseConstan.ROW_USER, "user");
        user.email = preferences.getString(DatabaseConstan.ROW_EMAIL, "email");
        user.password = preferences.getString(DatabaseConstan.ROW_PASSWORD, "password");
        return user;
    }

    public static void userLogout() {
        preferences.edit().putBoolean(PREF_LOGIN, false).apply();
    }

    public static void userLogin(ModelUser user) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(DatabaseConstan.ROW_ID, user.userID);
        editor.putString(DatabaseConstan.ROW_EMAIL, user.getEmail());
        editor.putString(DatabaseConstan.ROW_USER, user.getPassword());
        editor.putString(DatabaseConstan.ROW_PASSWORD, user.getPassword());
        editor.putBoolean(PREF_LOGIN, true);
        editor.apply();
    }
}
