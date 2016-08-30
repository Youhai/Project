package com.google.youhai.myinterviewproject.Application;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;


public class MyInterviewProject  extends Application{

    private Context context;
    private PermissionControl permissionControl;
    @Override
    public void registerComponentCallbacks(ComponentCallbacks callback) {
        super.registerComponentCallbacks(callback);
    }



    @Override
    public void onCreate() {
        super.onCreate();

        permissionControl = PermissionControl.getPermisionControl(getApplicationContext());

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
