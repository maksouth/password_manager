package com.example.maksouth.passwordmanager.infrastructure;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.maksouth.passwordmanager.entities.Credentials;
import com.example.maksouth.passwordmanager.entities.DummyItemCredentials;
import com.example.maksouth.passwordmanager.entities.ItemCredentials;
import com.example.maksouth.passwordmanager.entities.MasterCredentials;
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
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_APPEND;

/**
 * database communication is held in main thread.
 * So always create separate thread (e.g. using AsyncTask) for db operations
 */
public class SQLiteRepository implements Repository {

    private static final String LOG_TAG = "MY_TAG";

    private static final String DATABASE_NAME = "CREDENTIALS_DB";
    private static final String CREDENTIALS_TABLE_NAME = "CREDENTIALS";

    private static final String ID_COLUMN = "ID";
    private static final String NAME_COLUMN = "NAME";
    private static final String LOGIN_COLUMN = "LOGIN";
    private static final String PASSWORD_COLUMN = "PASSWORD";
    private static final String ADDRESS_COLUMN = "ADDRESS";
    private static final String DELETED_COLUMN = "DELETED";

    private static final String MASTER_PASSWORD_STORAGE_NAME = "MasterPasswordStorage";

    private static final String NO_DATA = "no_data";
    private static final String EQUALS = "=";
    /**
     * 1 - initial creation of table CREDENTIALS (id, name, login, password)
     * 3 - add new column in CREDENTIALS table - address
     * 4 - add new column in CREDENTIALS table - deleted
     */
    private static final int DATABASE_VERSION = 4;

    Context context;
    SQLiteOpenHelper dbHelper;
    SQLiteDatabase database;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private final String MASTER_PASSWORD_TAG = "MasterPasswordTag";


    @Override
    public boolean connect(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        sharedPreferences = context.getSharedPreferences(MASTER_PASSWORD_STORAGE_NAME, Context.MODE_PRIVATE);
        editor = context.getSharedPreferences(MASTER_PASSWORD_STORAGE_NAME, Context.MODE_PRIVATE).edit();
        return true;
    }

    @Override
    public void disconnect() {
        if(dbHelper!=null){
            dbHelper.close();
        }
        if(database != null){
            database.close();
        }
        sharedPreferences = null;
        editor = null;
    }

    @Override
    public void storeMasterPassword(Credentials masterPassword) throws InvalidClassException {
        editor.putString(MASTER_PASSWORD_TAG, masterPassword.getPassword());
        editor.commit();

    }

    @Override
    public boolean storeItem(ItemCredentials item) throws Exception {
        ContentValues cv = new ContentValues();
        cv.put(NAME_COLUMN, item.getName());
        cv.put(LOGIN_COLUMN, item.getLogin());
        cv.put(PASSWORD_COLUMN, item.getPassword());
        cv.put(ADDRESS_COLUMN, item.getAddress());
        long rowID = database.insert(CREDENTIALS_TABLE_NAME, null, cv);
        if(rowID == -1) throw new Exception(ExceptionMessages.CANT_SAVE_TO_DB);
        item.setId(rowID);
        return true;
    }

    @Override
    public void updateItem(long id, ItemCredentials newValue) throws Exception {
        ContentValues cv = new ContentValues();
        cv.put(NAME_COLUMN, newValue.getName());
        cv.put(LOGIN_COLUMN, newValue.getLogin());
        cv.put(PASSWORD_COLUMN, newValue.getPassword());
        cv.put(ADDRESS_COLUMN, newValue.getAddress());

        int rowsAffected = database.update(CREDENTIALS_TABLE_NAME, cv, ID_COLUMN+EQUALS+id, null);
        if(rowsAffected==0) throw new Exception(ExceptionMessages.NO_ITEMS_UPDATED);
    }

    @Override
    public void removeItem(long id) throws Exception {
        ItemCredentials newValue = getCredentials(id);
        ContentValues cv = new ContentValues();
        cv.put(NAME_COLUMN, newValue.getName());
        cv.put(LOGIN_COLUMN, newValue.getLogin());
        cv.put(PASSWORD_COLUMN, newValue.getPassword());
        cv.put(ADDRESS_COLUMN, newValue.getAddress());
        cv.put(DELETED_COLUMN, true);

        int itemsDeleted = database.update(CREDENTIALS_TABLE_NAME, cv, ID_COLUMN+EQUALS+id, null);
        if(itemsDeleted == 0) throw new Exception(ExceptionMessages.NO_ITEMS_DELETED);
    }

    @Override
    public MasterCredentials getMasterPassword() {
        if(sharedPreferences.getString(MASTER_PASSWORD_TAG, NO_DATA).equals(NO_DATA)){
            return null;
        }
        String password = sharedPreferences.getString(MASTER_PASSWORD_TAG, NO_DATA);
        MasterCredentials masterCredentials = new MasterCredentials(password);
        return masterCredentials;
    }

    @Override
    public List<ItemCredentials> getAllCredentials() {
        List<ItemCredentials> credentials = new ArrayList<>();
        ItemCredentials templateItem;
        Cursor cursor = database.query(CREDENTIALS_TABLE_NAME, null, DELETED_COLUMN+EQUALS+"'FALSE'", null, null, null, null);

        if(cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex(ID_COLUMN);
            int nameColIndex = cursor.getColumnIndex(NAME_COLUMN);
            int loginColIndex = cursor.getColumnIndex(LOGIN_COLUMN);
            int passwordColIndex = cursor.getColumnIndex(PASSWORD_COLUMN);
            int addressColIndex = cursor.getColumnIndex(ADDRESS_COLUMN);

            do{
                templateItem = new DummyItemCredentials();
                templateItem.setId(cursor.getLong(idColIndex));
                templateItem.setName(cursor.getString(nameColIndex));
                templateItem.setLogin(cursor.getString(loginColIndex));
                templateItem.setPassword(cursor.getString(passwordColIndex));
                templateItem.setAddress(cursor.getString(addressColIndex));

                credentials.add(templateItem);
                Log.d(LOG_TAG, templateItem.toString());
            }while (cursor.moveToNext());
            cursor.close();

        }

        return credentials;
    }

    @Override
    public ItemCredentials getCredentials(long id) {
        Cursor cursor = database.query(CREDENTIALS_TABLE_NAME, null, ID_COLUMN+EQUALS+id, null, null, null, null);
        ItemCredentials templateItem = null;
        if(cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex(ID_COLUMN);
            int nameColIndex = cursor.getColumnIndex(NAME_COLUMN);
            int loginColIndex = cursor.getColumnIndex(LOGIN_COLUMN);
            int passwordColIndex = cursor.getColumnIndex(PASSWORD_COLUMN);
            int addressColIndex = cursor.getColumnIndex(ADDRESS_COLUMN);

            templateItem = new DummyItemCredentials();
            templateItem.setId(cursor.getLong(idColIndex));
            templateItem.setName(cursor.getString(nameColIndex));
            templateItem.setLogin(cursor.getString(loginColIndex));
            templateItem.setPassword(cursor.getString(passwordColIndex));
            templateItem.setAddress(cursor.getString(addressColIndex));
        }
        return templateItem;
    }

    @Override
    public int clearAll() {
        int itemsDeleted = database.delete(CREDENTIALS_TABLE_NAME, null, null);
        Log.d(LOG_TAG, "Total deleted items: " + itemsDeleted);
        return itemsDeleted;
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            db.execSQL("create table " + CREDENTIALS_TABLE_NAME + " ("
                    + ID_COLUMN + " integer primary key autoincrement,"
                    + NAME_COLUMN + " text,"
                    + LOGIN_COLUMN + " text,"
                    + PASSWORD_COLUMN + " text,"
                    + ADDRESS_COLUMN + " text,"
                    + DELETED_COLUMN + " boolean DEFAULT FALSE);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table " + CREDENTIALS_TABLE_NAME);
            Log.d(LOG_TAG, "Table CREDENTIALS updated");
        }
    }
}
