package com.JonesRandom.LoginSQLite.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jonesrandom on 11/27/17.
 * <p>
 * AA
 */

public class DatabaseConstan {

    public static final String DATABASE_NAME = "db_user";
    public static final String DATABASE_TABLE = "tabel_user";

    public static final String ROW_ID = "_id";
    public static final String ROW_PASSWORD = "passwd";
    public static final String ROW_EMAIL = "email";
    public static final String ROW_USER = "user";

    public static final String QUERY_CREATE = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + "("
            + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ROW_USER + " TEXT,"
            + ROW_EMAIL + " TEXT,"
            + ROW_PASSWORD + " TEXT)";

    public static final String QUERY_UPGRADE = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

    public static String Login(String Email) {
        return "SELECT * FROM " + DATABASE_TABLE + " WHERE " + ROW_EMAIL + " ='" + Email + "'";
    }

    public static boolean cekUser(SQLiteDatabase database, String Email) {
        boolean userAvailable;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + ROW_EMAIL + " ='" + Email + "'", null);
            userAvailable = cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return userAvailable;
    }
}
