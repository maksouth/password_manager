package com.example.maksouth.passwordmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maksouth.passwordmanager.case_processed_activities.PasswordManagerActivity;
import com.example.maksouth.passwordmanager.entities.DummyItemCredentials;
import com.example.maksouth.passwordmanager.entities.ItemCredentials;
import com.example.maksouth.passwordmanager.entities.RealmItemCredentials;

public class AddItemActivity extends PasswordManagerActivity{

    public static final String ERROR_EMPTY_NAME = "Please, input name";
    public static final String ERROR_EMPTY_LOGIN = "Please, input login";
    public static final String ERROR_EMPTY_PASSWORD = "Please, input password";
    public static final String ERROR_SAVE_CREDENTIALS = "Something wrong!";
    public static final String SUCCESS_SAVE_CREDENTIALS = "New item added!";
    public static final String EMPTY_STRING = "";

    EditText nameET;
    EditText loginET;
    EditText passwordET;
    EditText addressET;
    Button saveButton;
    TextView statusTV;
    EditTextOnClickListener editTextOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        initialize();
        setListeners();
    }

    private void initialize(){
        editTextOnClickListener = new EditTextOnClickListener();
        nameET = (EditText) findViewById(R.id.name_field);
        loginET = (EditText) findViewById(R.id.login_field);
        passwordET = (EditText) findViewById(R.id.password_field);
        addressET = (EditText) findViewById(R.id.address_field);
        saveButton = (Button) findViewById(R.id.save_button);
        statusTV = (TextView) findViewById(R.id.status_text);
    }

    private void setListeners(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Save new item button pressed");
                String name = nameET.getText().toString().trim();
                String login = loginET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                String address = addressET.getText().toString().trim();

                ItemCredentials credentials = new RealmItemCredentials();
                credentials.setName(name);
                credentials.setLogin(login);
                credentials.setPassword(password);
                credentials.setAddress(address);

                try {
                    facade.addNewCredentialsItem(credentials);
                    Log.d(LOG_TAG, SUCCESS_SAVE_CREDENTIALS);
                    Toast.makeText(getApplicationContext(), SUCCESS_SAVE_CREDENTIALS, Toast.LENGTH_LONG).show();
                    onBackPressed();
                } catch (Exception e) {
                    Log.d(LOG_TAG, ERROR_SAVE_CREDENTIALS + " " + e.getMessage());
                    Toast.makeText(AddItemActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        nameET.setOnClickListener(editTextOnClickListener);
        loginET.setOnClickListener(editTextOnClickListener);
        passwordET.setOnClickListener(editTextOnClickListener);

    }

    private class EditTextOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            statusTV.setText(EMPTY_STRING);
        }
    }
}

