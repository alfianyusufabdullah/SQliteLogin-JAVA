package com.JonesRandom.LoginSQLite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;

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

    private static boolean databaseOpen = false;

    private DatabaseHelper(Context context) {
        super(context, DatabaseConstan.DATABASE_NAME, null, 1);
    }

    public static void initDatabaseHelper(Context context) {
        INSTANCE = new DatabaseHelper(context);
    }

    public static void closeDatabase() {
        if (databaseOpen && database.isOpen()) {
            database.close();
            databaseOpen = false;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseConstan.QUERY_CREATE);

        Log.i("Database", "onCreate: Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DatabaseConstan.QUERY_UPGRADE);
        onCreate(db);

        Log.i("Database", "onCreate: Database Upgraded");
    }

    public static class perform {

        public static void Login(final String Email, final String Password, final PerformCallback callback) {
            if (!databaseOpen) {
                database = INSTANCE.getWritableDatabase();
                databaseOpen = true;
            }

            if (Email.isEmpty()) {
                callback.inputError(0, "Masukkan Email Untuk Melanjutkan");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                callback.inputError(0, "Email Yang Kamu Masukkan Sepertinya Kurang Valid");
                return;
            }

            if (Password.isEmpty()) {
                callback.inputError(1, "Masukkan Password Untuk Melanjukan");
                return;
            }

            callback.progress();
            new Handler().postDelayed(() -> {
                Cursor cursor = null;
                try {
                    cursor = database.rawQuery(DatabaseConstan.Login(Email), null);
                    cursor.moveToFirst();
                    if (cursor.getCount() > 0) {
                        String password = cursor.getString(cursor.getColumnIndex(DatabaseConstan.ROW_PASSWORD));
                        if (Password.equals(password)) {

                            ModelUser user = new ModelUser();
                            user.userID = cursor.getInt(cursor.getColumnIndex(DatabaseConstan.ROW_ID));
                            user.username = cursor.getString(cursor.getColumnIndex(DatabaseConstan.ROW_USER));
                            user.email = cursor.getString(cursor.getColumnIndex(DatabaseConstan.ROW_EMAIL));
                            user.password = cursor.getString(cursor.getColumnIndex(DatabaseConstan.ROW_PASSWORD));

                            PreferenceManager.userLogin(user);

                            callback.success();

                        } else {
                            callback.failed(DatabaseConstan.SIGNIN_ERR_PASSWORD);
                        }
                    } else {
                        callback.failed(DatabaseConstan.SIGNIN_ERR_EMAIL);
                    }

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }, 3000);
        }

        public static void Register(final ModelUser user, final PerformCallback callback) {
            if (!databaseOpen) {
                database = INSTANCE.getWritableDatabase();
                databaseOpen = true;
            }

            if (user.getUser().isEmpty()) {
                callback.inputError(0, "Masukkan Username Untuk Melanjutkan");
                return;
            }

            if (user.getEmail().isEmpty()) {
                callback.inputError(1, "Masukkan Email Untuk Melanjutkan");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(user.getEmail()).matches()) {
                callback.inputError(1, "Email Yang Kamu Masukkan Sepertinya Kurang Valid");
                return;
            }

            if (user.getPassword().isEmpty()) {
                callback.inputError(2, "Masukkan Password Untuk Melanjutkan");
                return;
            }

            if (user.getPasswordConfirm().isEmpty()) {
                callback.inputError(3, "Konfirmasi Password Untuk Melanjutkan");
                return;
            }

            if (!user.getPasswordConfirm().equals(user.getPassword())) {
                callback.inputError(3, "Password Yang Kamu Masukkan Salah");
                return;
            }

            callback.progress();
            new Handler().postDelayed(() -> {
                boolean userAvailable = DatabaseConstan.cekUser(database, user.email);

                if (userAvailable) {
                    callback.failed(DatabaseConstan.REGISTER_ERR_EMAIL);
                } else {

                    ContentValues values = new ContentValues();
                    values.put(DatabaseConstan.ROW_USER, user.username);
                    values.put(DatabaseConstan.ROW_EMAIL, user.email);
                    values.put(DatabaseConstan.ROW_PASSWORD, user.password);

                    long stat = database.insert(DatabaseConstan.DATABASE_TABLE, null, values);
                    if (stat != 0) {
                        callback.success();
                    } else {
                        callback.failed(DatabaseConstan.REGISTER_ERROR);
                    }
                }
            }, 3000);
        }

        public interface PerformCallback {

            void inputError(int indexInput, String Error);

            void progress();

            void success();

            void failed(String error);
        }
    }
}
