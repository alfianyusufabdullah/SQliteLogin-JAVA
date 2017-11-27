package com.JonesRandom.LoginSQLite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;

import com.JonesRandom.LoginSQLite.common.PreferenceManager;
import com.JonesRandom.LoginSQLite.model.ModelUser;

/**
 * Created by JonesRandom on 18/09/2016.
 * <p>
 * AA
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper INSTANCE;
    private static SQLiteDatabase database;

    private DatabaseHelper(Context context) {
        super(context, DatabaseConstan.DATABASE_NAME, null, 1);
    }

    public static void initDatabaseHelper(Context context) {
        INSTANCE = new DatabaseHelper(context);
    }

    public static void closeDatabase() {
        if (database != null) {
            database.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseConstan.QUERY_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DatabaseConstan.QUERY_UPGRADE);
        onCreate(db);
    }

    public static class perform {

        public static void Login(final String Email, final String Password, final AuthCallback callback) {
            if (database == null) {
                database = INSTANCE.getWritableDatabase();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Cursor cursor = null;
                    try {
                        cursor = database.rawQuery(DatabaseConstan.Login(Email), null);

                        if (cursor.getCount() < 1) {
                            String password = cursor.getString(cursor.getColumnIndex(DatabaseConstan.ROW_PASSWORD));
                            if (Password.equals(password)) {

                                ModelUser user = new ModelUser();
                                user.userID = cursor.getInt(cursor.getColumnIndex(DatabaseConstan.ROW_ID));
                                user.user = cursor.getString(cursor.getColumnIndex(DatabaseConstan.ROW_USER));
                                user.email = cursor.getString(cursor.getColumnIndex(DatabaseConstan.ROW_EMAIL));
                                user.password = cursor.getString(cursor.getColumnIndex(DatabaseConstan.ROW_PASSWORD));

                                PreferenceManager.userLogin(user);

                                callback.loginSuccess();

                            } else {
                                callback.loginFailed("Password Yang Kamu Masukkan Salah");
                            }
                        } else {
                            callback.loginFailed("Email Tidak Terdaftar");
                        }

                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                }
            }, 3000);
        }

        public static void Register(final ModelUser user, final AuthCallback callback) {
            if (database == null) {
                database = INSTANCE.getWritableDatabase();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean userAvailable = DatabaseConstan.cekUser(database, user.email);

                    if (userAvailable) {
                        callback.loginFailed("Email Yang Kamu Masukkan Sudah Terdaftar");
                    } else {

                        ContentValues values = new ContentValues();
                        values.put(DatabaseConstan.ROW_USER, user.user);
                        values.put(DatabaseConstan.ROW_EMAIL, user.email);
                        values.put(DatabaseConstan.ROW_PASSWORD, user.password);

                        long stat = database.insert(DatabaseConstan.DATABASE_TABLE, null, values);
                        if (stat != 0) {
                            callback.loginSuccess();
                        } else {
                            callback.loginFailed("Terjadi Kesalahan Saat Melakukan Pendaftaran");
                        }
                    }
                }
            }, 3000);
        }
    }

    public interface AuthCallback {
        void loginSuccess();

        void loginFailed(String error);
    }
}
