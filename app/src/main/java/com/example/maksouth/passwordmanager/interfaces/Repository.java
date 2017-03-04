package com.example.maksouth.passwordmanager.interfaces;

import android.content.Context;

import com.example.maksouth.passwordmanager.entities.Credentials;
import com.example.maksouth.passwordmanager.entities.ItemCredentials;
import com.example.maksouth.passwordmanager.entities.MasterCredentials;

import java.io.InvalidClassException;
import java.util.List;

/**
 * Created by maksouth on 11.02.17.
 */

public interface Repository {

    boolean connect(Context context);
    void disconnect();
    void storeMasterPassword(Credentials masterPassword) throws InvalidClassException;
    boolean storeItem(ItemCredentials item) throws Exception;
    void updateItem(long id, ItemCredentials newValue) throws Exception;
    void removeItem(long id) throws Exception;
    Credentials getMasterPassword();
    List<ItemCredentials> getAllCredentials();
    ItemCredentials getCredentials(long id);
    int clearAll();
}
