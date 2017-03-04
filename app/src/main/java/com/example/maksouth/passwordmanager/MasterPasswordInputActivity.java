package com.example.maksouth.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.maksouth.passwordmanager.case_processed_activities.DoubleTapToExitActivity;
import com.example.maksouth.passwordmanager.interfaces.CredentialsManagerFacade;
import com.example.maksouth.passwordmanager.infrastructure.DummyCredentialsManagerFacade;

public class MasterPasswordInputActivity extends DoubleTapToExitActivity {

    CredentialsManagerFacade facade;
    EditText passwordET;
    TextView changeMasterPasswordTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        facade = DummyCredentialsManagerFacade.getInstance(getApplicationContext());
        initialize();
        setListeners();
    }

    public void initialize(){
        passwordET = (EditText) findViewById(R.id.password_field);
        changeMasterPasswordTV = (TextView) findViewById(R.id.change_master_password_text);
    }

    public void setListeners(){
        passwordET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(facade.authenticate(s.toString())){
                    finish();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        changeMasterPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MasterPasswordInputActivity.this, ChangeMasterPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        facade = null;
    }
}
