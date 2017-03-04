package com.example.maksouth.passwordmanager.credentials_manager;

import com.example.maksouth.passwordmanager.entities.ItemCredentials;
import com.example.maksouth.passwordmanager.infrastructure.ExceptionMessages;
import com.example.maksouth.passwordmanager.interfaces.Repository;

import java.util.List;

/**
 * Created by maksouth on 11.02.17.
 */

public class DummyCredentialsManager implements CredentialsManager {

    Repository repository;
    private static final String EMPTY_STRING = "";

    public DummyCredentialsManager(Repository repository){
        this.repository = repository;
    }

    @Override
    public boolean addNewItem(ItemCredentials credentials) throws Exception {
        if(!prepareForSaving(credentials)){
            throw new Exception(ExceptionMessages.INVALID_ITEM_FORMAT);
        }
        return repository.storeItem(credentials);
    }

    @Override
    public void removeCredentials(long id) throws Exception {
        repository.removeItem(id);
    }

    @Override
    public void updateCredentials(long id, ItemCredentials newCreds) throws Exception {
        repository.updateItem(id, newCreds);
    }

    @Override
    public List<ItemCredentials> getAllCredentialsItems() {
        return repository.getAllCredentials();
    }

    @Override
    public int clearAll() {
        return repository.clearAll();
    }

    private boolean prepareForSaving(ItemCredentials itemCredentials){
        if(!isValid(itemCredentials.getName())
                || !isValid(itemCredentials.getLogin())
                || !isValid(itemCredentials.getPassword())){ return false;}
        itemCredentials.setName(itemCredentials.getName().trim());
        itemCredentials.setPassword(itemCredentials.getPassword().trim());
        itemCredentials.setLogin(itemCredentials.getLogin().trim());
        itemCredentials.setAddress(itemCredentials.getAddress().trim());

        return true;
    }

    private boolean isValid(String string){
        return !(string == null || string.equals(EMPTY_STRING));
    }


}
