package com.JonesRandom.LoginSQLite.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.JonesRandom.LoginSQLite.R;
import com.JonesRandom.LoginSQLite.database.DatabaseConstan;
import com.JonesRandom.LoginSQLite.database.DatabaseHelper;
import com.JonesRandom.LoginSQLite.model.ModelUser;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    FragmentCallback fragmentCallback;
    InputMethodManager inputMethodManager;

    boolean registerProggress;

    @BindView(R.id.rootView)
    View view;

    @BindView(R.id.loading)
    ProgressBar loading;

    @BindViews({R.id.inputUsername, R.id.inputEmail, R.id.inputPassword, R.id.inputPasswordConfirm})
    List<TextInputLayout> inputLayout;

    @BindViews({R.id.etUsername, R.id.etEmail, R.id.etPassword, R.id.etPasswordConfirm})
    List<TextInputEditText> etInput;

    @OnClick({R.id.btnToSignin, R.id.btnRegister})
    public void onCLick(View v) {

        if (registerProggress) {
            Snackbar.make(view, DatabaseConstan.AUTH_ON_PROGRESS, Snackbar.LENGTH_SHORT).show();
        } else {

            switch (v.getId()) {
                case R.id.btnToSignin:
                    fragmentCallback.toLoginFragment();
                    break;
                case R.id.btnRegister:

                    ModelUser user = new ModelUser();
                    user.username = etInput.get(0).getText().toString();
                    user.email = etInput.get(1).getText().toString();
                    user.password = etInput.get(2).getText().toString();
                    user.passwordConfirm = etInput.get(3).getText().toString();

                    DatabaseHelper.perform.Register(user, new DatabaseHelper.perform.PerformCallback() {
                        @Override
                        public void inputError(int indexInput, String Error) {
                            inputLayout.get(indexInput).setError(Error);
                        }

                        @Override
                        public void progress() {
                            int i = 0;
                            for (EditText et : etInput) {
                                et.setEnabled(false);
                                et.clearFocus();

                                inputLayout.get(i).setErrorEnabled(false);
                                i++;
                            }

                            registerProggress = true;
                            loading.setVisibility(View.VISIBLE);
                            hideKeyboard(etInput.get(0));

                        }

                        @Override
                        public void success() {
                            Snackbar.make(view, DatabaseConstan.REGISTER_SUCCESS, Snackbar.LENGTH_SHORT).show();

                            for (EditText et : etInput) {
                                et.setEnabled(true);
                            }

                            loading.setVisibility(View.GONE);
                            registerProggress = false;
                        }

                        @Override
                        public void failed(String error) {
                            Snackbar.make(view, error, Snackbar.LENGTH_SHORT).show();

                            for (EditText et : etInput) {
                                et.setEnabled(true);
                            }

                            loading.setVisibility(View.GONE);
                            registerProggress = false;
                        }
                    });

                    break;
            }
        }
    }

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentCallback = (FragmentCallback) context;
        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void hideKeyboard(View view) {
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
