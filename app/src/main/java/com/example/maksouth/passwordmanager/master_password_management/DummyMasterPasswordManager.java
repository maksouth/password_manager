package com.example.maksouth.passwordmanager.master_password_management;

import com.example.maksouth.passwordmanager.entities.Credentials;
import com.example.maksouth.passwordmanager.entities.MasterCredentials;
import com.example.maksouth.passwordmanager.entities.RealmMasterCredentials;
import com.example.maksouth.passwordmanager.infrastructure.ExceptionMessages;
import com.example.maksouth.passwordmanager.interfaces.Repository;

import java.io.InvalidClassException;

/**
 * Created by maksouth on 11.02.17.
 */

public class DummyMasterPasswordManager implements MasterPasswordManager{

    private Repository repository;
    private boolean isAuthenticated;
    private Credentials masterPasswordCached;

    public DummyMasterPasswordManager(Repository repository){
        this.repository = repository;
    }

    @Override
    public boolean isFirstLaunch() {
        return repository.getMasterPassword() == null;
    }

    @Override
    public void resetPassword(Credentials oldCreds, Credentials newCreds) throws Exception {
        prepareForSaving(oldCreds);
        prepareForSaving(newCreds);

        if(authenticate(oldCreds)){
            repository.storeMasterPassword(newCreds);
            masterPasswordCached = null;
        }else{
            throw new Exception(ExceptionMessages.WRONG_PASSWORD);
        }
    }

    @Override
    public boolean initialEnter(Credentials credentials) {
        if(repository.getMasterPassword() == null){
            try {
                repository.storeMasterPassword(credentials);
            } catch (InvalidClassException e) {
                e.printStackTrace();
            }
            isAuthenticated = true;
        }
        return true;
    }

    @Override
    public boolean authenticate(Credentials credentials) {
        if(masterPasswordCached == null) {
            masterPasswordCached = repository.getMasterPassword();
        }
        isAuthenticated = masterPasswordCached.equals(credentials);
        return isAuthenticated;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void unauthenticate() {
        isAuthenticated = false;
    }

    void prepareForSaving(Credentials credentials) throws Exception {
        String password = credentials.getPassword();
        if(password == null || password.equals("")){
            throw new Exception(ExceptionMessages.WRONG_PASSWORD_FORMAT);
        }
        credentials.setPassword(password.trim());
    }

}
