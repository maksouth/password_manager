package com.example.maksouth.passwordmanager.infrastructure;

import android.content.Context;
import android.util.Log;

import com.example.maksouth.passwordmanager.entities.Credentials;
import com.example.maksouth.passwordmanager.entities.ItemCredentials;
import com.example.maksouth.passwordmanager.interfaces.Repository;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InvalidClassException;
import java.util.List;

import static android.content.Context.MODE_APPEND;

/**
 * Created by maksouth on 28.02.17.
 */

public class LogDecoratorRepository implements Repository {

    private Repository repository;
    private static final String UPDATE_FLAG = "^^^";
    private static final String REMOVE_FLAG = "---";
    private static final String ADD_FLAG = "+++";
    private static final String SPACE = " ";
    private static final String NEW_LINE = "\n";
    private static final String FILE_NAME = "items_change_history.txt";
    private static final String LOG_TAG = "MY_TAG";

    File file;
    BufferedReader reader;
    FileOutputStream writer = null;
    FileInputStream fis;
    Gson gson;

    public LogDecoratorRepository(Repository repository){
        this.repository = repository;
    }

    @Override
    public boolean connect(Context context) {

        try {
            file = new File(context.getFilesDir(), FILE_NAME);
            gson = new Gson();
            if (!file.exists()) {
                file.createNewFile();
            }

            writer = context.openFileOutput(FILE_NAME, MODE_APPEND);
            reader = new BufferedReader(new FileReader(file));

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return repository.connect(context);

    }

    @Override
    public void disconnect() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        repository.disconnect();
    }

    @Override
    public void storeMasterPassword(Credentials masterPassword) throws InvalidClassException {
        repository.storeMasterPassword(masterPassword);
    }

    @Override
    public boolean storeItem(ItemCredentials item) throws Exception {
        writeChangesToJson(item, ADD_FLAG);
        return repository.storeItem(item);
    }

    @Override
    public void updateItem(long id, ItemCredentials newValue) throws Exception {
        writeChangesToJson(newValue, UPDATE_FLAG);
        repository.updateItem(id, newValue);
    }

    @Override
    public void removeItem(long id) throws Exception {
        writeChangesToJson(getCredentials(id), REMOVE_FLAG);
        repository.removeItem(id);
    }

    @Override
    public Credentials getMasterPassword() {
        return repository.getMasterPassword();
    }

    @Override
    public List<ItemCredentials> getAllCredentials() {
        String string;
        try {
            while((string = reader.readLine()) != null){
                Log.d(LOG_TAG, "File read: " + string);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return repository.getAllCredentials();
    }

    @Override
    public ItemCredentials getCredentials(long id) {
        return repository.getCredentials(id);
    }

    @Override
    public int clearAll() {
        return repository.clearAll();
    }

    private void writeChangesToJson(ItemCredentials item, String flag) throws IOException {
        String jsonItem = gson.toJson(item);
        Log.d(LOG_TAG, "Logs " + flag + " " + jsonItem);
        try{
            writer.write((flag+SPACE+jsonItem + NEW_LINE).getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
