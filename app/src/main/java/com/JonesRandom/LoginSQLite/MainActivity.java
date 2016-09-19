package com.JonesRandom.LoginSQLite;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DBHandler handler;

    EditText user, passwd;
    Button masuk, daftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new DBHandler(this);

        user = (EditText) findViewById(R.id.editNamaPengguna);
        passwd = (EditText) findViewById(R.id.editKataSandi);

        masuk = (Button) findViewById(R.id.btnMasuk);
        daftar = (Button) findViewById(R.id.btnDaftar);

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = user.getText().toString().trim();
                String Passwd = passwd.getText().toString().trim();

                if (Username.equals("") || Passwd.equals("")) {
                    dialog("Login","Field Nama Pengguna/Kata Sandi Kosong");
                } else {
                    Cursor cur = handler.mLogin(Username);
                    if (cur.getCount() < 1) {
                        dialog("Login ","Tidak ada Pengguna dengan Nama " + Username + ", Silahkan Daftar");
                    } else {
                        cur.moveToFirst();
                        String Konfirmasi = cur.getString(cur.getColumnIndex(DBHandler.row_passwd));
                        long id = cur.getLong(cur.getColumnIndex(DBHandler.row_id));
                        if (Passwd.equals(Konfirmasi)) {
                            handler.SessionManager(true,Username);
                            startActivity(new Intent(MainActivity.this, User.class));
                            finish();
                        } else {
                            dialog("Login Error","Kata Sandi Salah");
                        }
                    }
                }
            }
        });

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = user.getText().toString().trim();
                String Passwd = passwd.getText().toString().trim();

                if (Username.equals("") || Passwd.equals("")) {
                    dialog("Daftar","Field Nama Pengguna/Kata Sandi Kosong");
                } else {
                    Cursor cur = handler.mLogin(Username);
                    if (cur.getCount() < 1) {
                        handler.Daftar(Username, Passwd);
                        dialog("Daftar","Nama Pengguna " + Username + " Berhasil Di Buat, Silahkan Masuk Untuk Melanjutkan");
                    } else {
                        dialog("Daftar","Nama Pengguna " + Username + " Sudah Ada, Cobalah Dengan Nama Lain");
                    }
                }
            }
        });
    }

    public void dialog(String judul,String pesan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(judul);
        builder.setMessage(pesan);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
