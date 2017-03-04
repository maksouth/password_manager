package com.example.maksouth.passwordmanager.master_password_management;

import com.example.maksouth.passwordmanager.entities.Credentials;

/**
 * Created by maksouth on 11.02.17.
 */

public interface MasterPasswordManager {
    boolean isFirstLaunch();
    void resetPassword(Credentials oldCreds, Credentials newCreds) throws Exception;
    boolean initialEnter(Credentials credentials);
    boolean authenticate(Credentials credentials);
    boolean isAuthenticated();
    void unauthenticate();
}
