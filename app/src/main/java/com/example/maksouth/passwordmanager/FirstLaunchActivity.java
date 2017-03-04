package com.example.maksouth.passwordmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.maksouth.passwordmanager.case_processed_activities.DoubleTapToExitActivity;
import com.example.maksouth.passwordmanager.entities.Credentials;
import com.example.maksouth.passwordmanager.entities.MasterCredentials;
import com.example.maksouth.passwordmanager.entities.RealmMasterCredentials;
import com.example.maksouth.passwordmanager.interfaces.CredentialsManagerFacade;
import com.example.maksouth.passwordmanager.infrastructure.DummyCredentialsManagerFacade;

public class FirstLaunchActivity extends DoubleTapToExitActivity {

    CredentialsManagerFacade facade;
    TextView statusTV;
    EditText masterPasswordFieldET;
    Button savePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);
        facade = DummyCredentialsManagerFacade.getInstance(getApplicationContext());

        masterPasswordFieldET = (EditText) findViewById(R.id.password_field);
        statusTV = (TextView) findViewById(R.id.status_text);
        findViewById(R.id.save_password_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Credentials credentials = new RealmMasterCredentials(masterPasswordFieldET.getText().toString());
                boolean isFirstLaunchValid = facade.validateFirstLaunch(credentials);
                if(isFirstLaunchValid){
                    finish();
                }else{
                    statusTV.setText("try again");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        facade = null;
    }


}
