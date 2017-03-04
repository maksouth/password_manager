package com.example.maksouth.passwordmanager.credentials_manager;

import com.example.maksouth.passwordmanager.entities.ItemCredentials;

import java.util.List;

/**
 * Created by maksouth on 11.02.17.
 */

public interface CredentialsManager {
    boolean addNewItem(ItemCredentials credentials) throws Exception;
    void removeCredentials(long id) throws Exception;
    void updateCredentials(long id, ItemCredentials newCreds) throws Exception;
    List<ItemCredentials> getAllCredentialsItems();
    int clearAll();
}
