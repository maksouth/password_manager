package com.example.maksouth.passwordmanager.interfaces;

import com.example.maksouth.passwordmanager.entities.Credentials;
import com.example.maksouth.passwordmanager.entities.ItemCredentials;
import com.example.maksouth.passwordmanager.entities.MasterCredentials;

import java.util.List;

/**
 * Created by maksouth on 11.02.17.
 */

public interface CredentialsManagerFacade {
    boolean authenticate(String password);
    void unauthenticate();
    List<ItemCredentials> getAllCredentials();
    void addNewCredentialsItem(ItemCredentials item) throws Exception;
    void updateItem(ItemCredentials item) throws Exception;
    void deleteItem(ItemCredentials item) throws Exception;
    int deleteAllItems();
    boolean isAuthenticated();
    boolean isFirstLaunch();
    boolean validateFirstLaunch(Credentials credentials);
    ItemCredentials getCredentialById(long id);
    void changeMasterPassword(Credentials oldPassword, Credentials newPassword) throws Exception;
}
