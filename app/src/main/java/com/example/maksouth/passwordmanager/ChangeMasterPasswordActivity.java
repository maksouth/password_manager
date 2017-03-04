package com.example.maksouth.passwordmanager;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.system.ErrnoException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maksouth.passwordmanager.case_processed_activities.PasswordManagerActivity;
import com.example.maksouth.passwordmanager.entities.Credentials;
import com.example.maksouth.passwordmanager.entities.DummyItemCredentials;
import com.example.maksouth.passwordmanager.entities.ItemCredentials;
import com.example.maksouth.passwordmanager.entities.MasterCredentials;
import com.example.maksouth.passwordmanager.infrastructure.DummyCredentialsManagerFacade;
import com.example.maksouth.passwordmanager.infrastructure.ExceptionMessages;
import com.example.maksouth.passwordmanager.interfaces.CredentialsManagerFacade;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class ChangeMasterPasswordActivity extends AppCompatActivity {

    CredentialsManagerFacade facade;
    EditText oldPasswordET;
    EditText newPasswordET;
    Button changePasswordButton;
    String PASSWORD_CHANGED = "Password changed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_master_password);

        initialize();
        setListeners();
    }

    void initialize(){
        facade = DummyCredentialsManagerFacade.getInstance(getApplicationContext());
        oldPasswordET = (EditText) findViewById(R.id.old_password_field);
        newPasswordET = (EditText) findViewById(R.id.new_password_field);
        changePasswordButton = (Button) findViewById(R.id.change_button);
    }

    void setListeners(){
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Credentials oldCredentials = new MasterCredentials(oldPasswordET.getText().toString());
                    Credentials newCredentials = new MasterCredentials(newPasswordET.getText().toString());
                    facade.changeMasterPassword(oldCredentials, newCredentials);
                    Toast.makeText(ChangeMasterPasswordActivity.this, PASSWORD_CHANGED, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }catch (Exception e){
                    if(e.getMessage().equals(ExceptionMessages.WRONG_PASSWORD)) {
                        oldPasswordET.setError(e.getMessage());
                    }else {
                        Toast.makeText(ChangeMasterPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
