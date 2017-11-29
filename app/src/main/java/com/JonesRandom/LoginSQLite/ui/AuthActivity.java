package com.JonesRandom.LoginSQLite.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.JonesRandom.LoginSQLite.R;
import com.JonesRandom.LoginSQLite.database.DatabaseHelper;
import com.JonesRandom.LoginSQLite.ui.fragment.FragmentCallback;
import com.JonesRandom.LoginSQLite.ui.fragment.LoginFragment;
import com.JonesRandom.LoginSQLite.ui.fragment.RegisterFragment;

public class AuthActivity extends AppCompatActivity implements FragmentCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if (savedInstanceState == null) {
            replaceFragment(new LoginFragment());
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public void toRegisterFragment() {
        replaceFragment(new RegisterFragment());
    }

    @Override
    public void toLoginFragment() {
        replaceFragment(new LoginFragment());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DatabaseHelper.closeDatabase();
    }

}
