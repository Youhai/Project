package com.google.youhai.myinterviewproject.Application;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


public class PermissionControl {
    private static PermissionControl instance;
    private static Context appContext;


    public static PermissionControl getPermisionControl(Context context){
        if(instance == null){
            instance = new PermissionControl();
            appContext = context;
        }
        return instance;
    }

    public static void checkPermission(Activity activity, String permission, int permission_case){

        int result = ContextCompat.checkSelfPermission(appContext, permission);
        if (result != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, permission_case);
            }else {

                ActivityCompat.requestPermissions(activity, new String[]{permission}, permission_case);
            }
        }else{
            Toast.makeText(activity,permission +" permission granted",Toast.LENGTH_LONG).show();
        }

    }

    public static boolean isGranted(String permission){
        int result = ContextCompat.checkSelfPermission(appContext, permission);
       return result == PackageManager.PERMISSION_GRANTED;
    }

}

