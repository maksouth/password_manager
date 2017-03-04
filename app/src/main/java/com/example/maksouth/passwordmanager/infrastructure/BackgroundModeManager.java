package com.example.maksouth.passwordmanager.infrastructure;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.Log;

/**
 * Created by maksouth on 12.02.17.
 */

public class BackgroundModeManager implements ComponentCallbacks2{

    public static final String TAG = "MY_TAG";
    private Handler callbackHandler;

    public BackgroundModeManager(Handler handler){
        callbackHandler = handler;
    }

    @Override
    public void onTrimMemory(int level) {
        if(level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN){
            Log.d(TAG, "onTrimMemory");
            callbackHandler.sendEmptyMessage(level);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }
}
