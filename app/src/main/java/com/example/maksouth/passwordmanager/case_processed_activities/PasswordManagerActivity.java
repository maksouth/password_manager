package com.example.maksouth.passwordmanager.case_processed_activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.maksouth.passwordmanager.FirstLaunchActivity;
import com.example.maksouth.passwordmanager.MasterPasswordInputActivity;
import com.example.maksouth.passwordmanager.infrastructure.BackgroundModeManager;
import com.example.maksouth.passwordmanager.infrastructure.DummyCredentialsManagerFacade;
import com.example.maksouth.passwordmanager.interfaces.CredentialsManagerFacade;

public class PasswordManagerActivity extends AppCompatActivity {

    public static boolean isMemoryTrimCallBackRegistered = false;
    public static final String LOG_TAG = "MY_TAG";
    public CredentialsManagerFacade facade;
    BackgroundModeManager backgroundModeManager;
    Handler backgroundModeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(facade != null){
                Log.d(LOG_TAG, "PasswordManagerActivity unathenticate");
                facade.unauthenticate();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facade = DummyCredentialsManagerFacade.getInstance(getApplicationContext());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && !isMemoryTrimCallBackRegistered){
            backgroundModeManager = new BackgroundModeManager(backgroundModeHandler);
            registerComponentCallbacks(backgroundModeManager);
            isMemoryTrimCallBackRegistered = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(facade.isFirstLaunch()){
            startActivity(new Intent(getApplicationContext(), FirstLaunchActivity.class));
        }else if(!facade.isAuthenticated()) {
            startActivity(new Intent(getApplicationContext(), MasterPasswordInputActivity.class));
        }
    }
}
