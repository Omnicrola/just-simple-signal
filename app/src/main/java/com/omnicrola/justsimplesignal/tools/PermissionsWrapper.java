package com.omnicrola.justsimplesignal.tools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.security.AndroidPermission;
import android.support.v4.app.ActivityCompat;

public class PermissionsWrapper {

    private final Context context;

    public PermissionsWrapper(Context context) {
        this.context = context;
    }

    public boolean hasPermission(AndroidPermission permission) {
        int result = ActivityCompat.checkSelfPermission(this.context, permission.toString());
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void request(Activity activity, AndroidPermission permission) {
        if (!hasPermission(permission)) {
            ActivityCompat.requestPermissions(activity, new String[]{permission.toString()}, 1);
        }
    }
}
