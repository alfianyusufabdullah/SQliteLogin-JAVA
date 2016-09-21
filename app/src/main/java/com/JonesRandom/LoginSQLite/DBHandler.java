package com.JonesRandom.LoginSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JonesRandom on 18/09/2016.
 */

public class DBHandler extends SQLiteOpenHelper {

    public static final String Nama_Database = "db_user";
    public static final String Nama_Tabel = "tabel_user";

    public static final String row_id = "_id";
    public static final String row_passwd = "passwd";
    public static final String row_username = "user";

    SharedPreferences pref;

    public DBHandler(Context context) {
        super(context, Nama_Database, null, 1);
        SQLiteDatabase db = getWritableDatabase();
        pref = context.getSharedPreferences("login_session", Context.MODE_PRIVATE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Nama_Tabel + "(" + row_id + " integer primary key autoincrement," + row_username + " text," + row_passwd + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + Nama_Tabel);
        onCreate(db);
    }

    public Cursor mLogin(String User) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery("select * from " + Nama_Tabel + " where " + row_username + "='" + User + "'", null);
        return cur;
    }

    public void Daftar(String User, String passwd) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(row_username,User);
        values.put(row_passwd,passwd);
        db.insert(Nama_Tabel,null,values);
    }

    public void SessionManager(boolean Status, String user) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("status_login", Status);
        editor.putString("user_id", user);
        editor.apply();
    }
}
