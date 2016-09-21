package com.JonesRandom.LoginSQLite;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class User extends AppCompatActivity {

    TextView id, user, passwd;
    Button keluar;
    DBHandler handler;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        handler = new DBHandler(this);

        pref = getSharedPreferences("login_session",MODE_PRIVATE);

        id = (TextView)findViewById(R.id.txtID);
        user = (TextView)findViewById(R.id.txtUser);
        passwd = (TextView)findViewById(R.id.txtPasswd);

        keluar = (Button)findViewById(R.id.btnKeluar);
        loadData();

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(User.this);
                builder.setCancelable(false);
                builder.setTitle("Logout");
                builder.setMessage("Apakah Anda Yakin Ingin Keluar ?");
                builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        handler.SessionManager(false,null);
                        startActivity(new Intent(User.this,MainActivity.class));
                        finish();
                    }
                });
                builder.setNegativeButton("tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        builder.setCancelable(true);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    public void loadData() {
        String Username = pref.getString("user_id",null);
        Cursor cur = handler.mLogin(Username);
        cur.moveToFirst();

        String mID = cur.getString(cur.getColumnIndex(DBHandler.row_id));
        String mUSER = cur.getString(cur.getColumnIndex(DBHandler.row_username));
        String mPASSWD = cur.getString(cur.getColumnIndex(DBHandler.row_passwd));

        id.setText("USER ID :\n" + mID);
        user.setText("Nama Pengguna : " + mUSER);
        passwd.setText("Kata Sandi : " + mPASSWD);
    }
}
