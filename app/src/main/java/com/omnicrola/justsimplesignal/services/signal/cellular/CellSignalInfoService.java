package com.omnicrola.justsimplesignal.services.signal.cellular;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.omnicrola.justsimplesignal.tools.PermissionsWrapper;

import java.util.Timer;

public class CellSignalInfoService extends Service {


    private Timer serviceTimer;

    @Override
    public void onCreate() {
        Log.d(getClass().getSimpleName(), "Starting...");

        if (serviceTimer != null) {
            serviceTimer.cancel();
        } else {
            serviceTimer = new Timer();
        }

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        CellSignalSampler cellSignalSampler = new CellSignalSampler(new PermissionsWrapper(getApplicationContext()), telephonyManager);
        serviceTimer.scheduleAtFixedRate(new SampleCellSignalTask(cellSignalSampler), 0, 250);
        Log.d(getClass().getSimpleName(), "Scheduled task");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
