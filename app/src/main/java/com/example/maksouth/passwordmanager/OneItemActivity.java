package com.example.maksouth.passwordmanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maksouth.passwordmanager.case_processed_activities.PasswordManagerActivity;
import com.example.maksouth.passwordmanager.entities.ItemCredentials;

public class OneItemActivity extends PasswordManagerActivity {

    public static final String ITEM_CREDENTIALS_ID_KEY = "ItemCredentialsId";
    public static final int ITEM_CREDENTIALS_ID_DEFAULT = 0;
    public static final String DELETE_DIALOG_TITLE = "Confirm delete?";
    public static final String DELETE_DIALOG_MESSAGE = "You will lose the record.";
    public static final String DELETE_DIALOG_YES_BUTTON = "Yes";
    public static final String DELETE_DIALOG_NO_BUTTON = "No";
    public static final String ITEM_SAVED = "Changes saved";
    public static final String COPIED_TO_CLIPBOARD = "Copied to clipboard";

    Toolbar menuToolbar;
    EditText nameET;
    EditText loginET;
    EditText passwordET;
    EditText addressET;
    KeyListener nameFieldKL;
    KeyListener loginFieldKL;
    KeyListener passwordFieldKL;
    KeyListener addressFieldKL;
    ItemCredentials itemCredentials;
    AlertDialog.Builder confirmDeleteDialog;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_item);
        initialize();
        setValues();
        setListeners();
    }

    private void initialize(){
        menuToolbar = (Toolbar) findViewById(R.id.one_item_toolbar);
        setSupportActionBar(menuToolbar);

        nameET = (EditText) findViewById(R.id.name_field);
        loginET = (EditText) findViewById(R.id.login_field);
        passwordET = (EditText) findViewById(R.id.password_field);
        addressET = (EditText) findViewById(R.id.address_field);

        confirmDeleteDialog = new AlertDialog.Builder(this);
        confirmDeleteDialog.setCancelable(true);
    }

    private void setValues(){
        long itemId = getIntent().getLongExtra(ITEM_CREDENTIALS_ID_KEY,
                ITEM_CREDENTIALS_ID_DEFAULT);
        itemCredentials = facade.getCredentialById(itemId);
        nameET.setText(itemCredentials.getName());
        loginET.setText(itemCredentials.getLogin());
        passwordET.setText(itemCredentials.getPassword());
        addressET.setText(itemCredentials.getAddress());

        confirmDeleteDialog.setTitle(DELETE_DIALOG_TITLE);  // заголовок
        confirmDeleteDialog.setMessage(DELETE_DIALOG_MESSAGE); // сообщение

        disableFields();
    }

    private void setListeners(){
        confirmDeleteDialog.setPositiveButton(DELETE_DIALOG_YES_BUTTON, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                try {
                    facade.deleteItem(itemCredentials);
                    onBackPressed();
                } catch (Exception e) {
                    Toast.makeText(OneItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        confirmDeleteDialog.setNegativeButton(DELETE_DIALOG_NO_BUTTON, null);

        loginET.setOnLongClickListener(new TextFieldsLongPressListener());
        passwordET.setOnLongClickListener(new TextFieldsLongPressListener());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.one_item_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        menu.findItem(R.id.save_menu_item).setEnabled(false);
        this.menu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_menu_item:
                Log.d(LOG_TAG, "EDIT MENU ITEM CLICKED!");
                menu.findItem(R.id.save_menu_item).setEnabled(true);
                enableFields();
                return true;
            case R.id.save_menu_item:
                Log.d(LOG_TAG, "SAVE MENU ITEM CLICKED!");
                disableFields();
                try {
                    saveChanges();
                    Toast.makeText(this, ITEM_SAVED, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                menu.findItem(R.id.save_menu_item).setEnabled(false);
                return true;
            case R.id.delete_menu_item:
                confirmDeleteDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void enableFields(){
        nameET.setKeyListener(nameFieldKL);
        loginET.setKeyListener(loginFieldKL);
        passwordET.setKeyListener(passwordFieldKL);
        addressET.setKeyListener(addressFieldKL);
    }

    private void disableFields(){
        nameFieldKL = nameET.getKeyListener();
        loginFieldKL = loginET.getKeyListener();
        passwordFieldKL = passwordET.getKeyListener();
        addressFieldKL = addressET.getKeyListener();

        nameET.setKeyListener(null);
        loginET.setKeyListener(null);
        passwordET.setKeyListener(null);
        addressET.setKeyListener(null);
    }

    private void saveChanges() throws Exception{
        itemCredentials.setName(nameET.getText().toString());
        itemCredentials.setLogin(loginET.getText().toString());
        itemCredentials.setPassword(passwordET.getText().toString());
        itemCredentials.setAddress(addressET.getText().toString());

        facade.updateItem(itemCredentials);
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public boolean copyToClipboard(String text) {
            int sdk = Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.HONEYCOMB) {
                ClipboardManager clipboard = (ClipboardManager) this
                        .getSystemService(this.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) this
                        .getSystemService(this.CLIPBOARD_SERVICE);
                ClipData clip = ClipData
                        .newPlainText(
                                "label", text);
                clipboard.setPrimaryClip(clip);
            }
            return true;
    }

    private class TextFieldsLongPressListener implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View v) {
            try{
                copyToClipboard(((EditText)v).getText().toString());
                Toast.makeText(OneItemActivity.this, COPIED_TO_CLIPBOARD, Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, COPIED_TO_CLIPBOARD + " " + ((EditText)v).getText().toString());
            }catch (Exception e){
                Toast.makeText(OneItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Error while clipboarding " + e.getMessage());
                return false;
            }
            return true;
        }
    }

}
