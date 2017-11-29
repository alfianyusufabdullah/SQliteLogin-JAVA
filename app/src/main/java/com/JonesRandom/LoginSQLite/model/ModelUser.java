package com.JonesRandom.LoginSQLite.model;

/**
 * Created by jonesrandom on 11/27/17.
 * <p>
 * AA
 */

public class ModelUser {

    public int userID;
    public String email;
    public String password;
    public String passwordConfirm;
    public String username;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public String getUser() {
        return username;
    }

    public int getUserID() {
        return userID;
    }
}
