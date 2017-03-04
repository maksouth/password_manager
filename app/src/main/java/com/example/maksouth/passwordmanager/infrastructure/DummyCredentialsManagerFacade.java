package com.example.maksouth.passwordmanager.infrastructure;

import android.content.Context;

import com.example.maksouth.passwordmanager.credentials_manager.CredentialsManager;
import com.example.maksouth.passwordmanager.credentials_manager.DummyCredentialsManager;
import com.example.maksouth.passwordmanager.entities.Credentials;
import com.example.maksouth.passwordmanager.entities.ItemCredentials;
import com.example.maksouth.passwordmanager.entities.MasterCredentials;
import com.example.maksouth.passwordmanager.interfaces.CredentialsManagerFacade;
import com.example.maksouth.passwordmanager.interfaces.Repository;
import com.example.maksouth.passwordmanager.master_password_management.DummyMasterPasswordManager;
import com.example.maksouth.passwordmanager.master_password_management.MasterPasswordManager;

import java.util.List;

/**
 * Created by maksouth on 11.02.17.
 */

public class DummyCredentialsManagerFacade implements CredentialsManagerFacade {

    private static CredentialsManagerFacade credentialsManagerFacade;
    private Repository originalRepository;
    private Repository repository;
    private MasterPasswordManager masterPasswordManager;
    private CredentialsManager credentialsManager;

    public static CredentialsManagerFacade getInstance(Context context){
        if(credentialsManagerFacade == null) {
            synchronized (DummyCredentialsManagerFacade.class) {
                if (credentialsManagerFacade == null){
                    credentialsManagerFacade = new DummyCredentialsManagerFacade(context);
                }
            }
        }
        return credentialsManagerFacade;
    }

    private DummyCredentialsManagerFacade(Context context){
        originalRepository = new SQLiteRepository();
        repository = new LogDecoratorRepository(originalRepository);
        repository.connect(context);
        masterPasswordManager = new DummyMasterPasswordManager(repository);
        credentialsManager = new DummyCredentialsManager(repository);
    }

    @Override
    public boolean authenticate(String password) {
        MasterCredentials masterCredentials = new MasterCredentials(password);
        return masterPasswordManager.authenticate(masterCredentials);
    }

    @Override
    public void unauthenticate() {
        masterPasswordManager.unauthenticate();
    }

    @Override
    public List<ItemCredentials> getAllCredentials() {
        return credentialsManager.getAllCredentialsItems();
    }

    @Override
    public void addNewCredentialsItem(ItemCredentials item) throws Exception {
        credentialsManager.addNewItem(item);
    }

    @Override
    public void updateItem(ItemCredentials item) throws Exception {
        credentialsManager.updateCredentials(item.getId(), item);
    }

    @Override
    public void deleteItem(ItemCredentials item) throws Exception {
        credentialsManager.removeCredentials(item.getId());
    }

    @Override
    public int deleteAllItems() {
        int itemsDeleted = credentialsManager.clearAll();
        return itemsDeleted;
    }

    @Override
    public boolean isAuthenticated() {
        return masterPasswordManager.isAuthenticated();
    }

    @Override
    public boolean isFirstLaunch() {
        return masterPasswordManager.isFirstLaunch();
    }

    @Override
    public boolean validateFirstLaunch(Credentials credentials) {
        boolean validateInitialEnter = masterPasswordManager.initialEnter(credentials);
        return validateInitialEnter;
    }

    @Override
    public ItemCredentials getCredentialById(long id) {
        return repository.getCredentials(id);
    }

    @Override
    public void changeMasterPassword(Credentials oldPassword, Credentials newPassword) throws Exception {
        masterPasswordManager.resetPassword(oldPassword, newPassword);
    }

    @Override
    protected void finalize() throws Throwable {
        repository.disconnect();
        credentialsManagerFacade = null;
        super.finalize();
    }
}
