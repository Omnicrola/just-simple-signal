package com.omnicrola.justsimplesignal.services.signal.cellular;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.SystemClock;
import android.security.AndroidPermission;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.omnicrola.justsimplesignal.data.CellSignal;
import com.omnicrola.justsimplesignal.tools.OsVersion;
import com.omnicrola.justsimplesignal.tools.PermissionsWrapper;
import com.omnicrola.justsimplesignal.tools.util.Possible;

import java.util.ArrayList;
import java.util.List;

public class CellSignalSampler {

    private PermissionsWrapper permissionsWrapper;
    private final TelephonyManager telephonyManager;

    public CellSignalSampler(PermissionsWrapper permissionsWrapper, TelephonyManager telephonyManager) {
        this.permissionsWrapper = permissionsWrapper;
        this.telephonyManager = telephonyManager;
    }

    public List<CellSignal> sample() {
        ArrayList<CellSignal> cellSignals = new ArrayList<>();

        Log.d(getClass().getSimpleName(), "Getting cell info...");
        if (permissionsWrapper.hasPermission(AndroidPermission.ACCESS_COARSE_LOCATION)) {
            Log.d(getClass().getSimpleName(), "Permissions granted for coarse location");
            @SuppressLint("MissingPermission")
            List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
            Log.d(getClass().getSimpleName(), "Found " + allCellInfo.size() + " CellInfo");
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo.isRegistered()) {
                    Possible<CellSignal> cellSignal = getSignalStrength(cellInfo);
                    if (cellSignal.isPresent()) {
                        cellSignals.add(cellSignal.get());
                    }
                }
            }
        } else {
            Log.d(getClass().getSimpleName(), "Permission denied for coarse location");
        }
        return cellSignals;
    }

    private Possible<CellSignal> getSignalStrength(CellInfo cellInfo) {
        boolean typeFound = false;
        int strength = Integer.MIN_VALUE;
        int signalId = Integer.MIN_VALUE;

        if (cellInfo instanceof CellInfoCdma) {
            Log.d(getClass().getSimpleName(), "Found CellInfoCdma");
            CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfo;
            strength = cellInfoCdma.getCellSignalStrength().getDbm();
            signalId = cellInfoCdma.getCellIdentity().getSystemId();
            typeFound = true;
        } else if (cellInfo instanceof CellInfoGsm) {
            Log.d(getClass().getSimpleName(), "Found CellInfoGsm");
            CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
            strength = cellInfoGsm.getCellSignalStrength().getDbm();
            signalId = cellInfoGsm.getCellIdentity().getCid();
            typeFound = true;
        } else if (cellInfo instanceof CellInfoLte) {
            Log.d(getClass().getSimpleName(), "Found CellInfoLte");
            CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
            strength = cellInfoLte.getCellSignalStrength().getDbm();
            signalId = cellInfoLte.getCellIdentity().getCi();
            typeFound = true;
        } else if (cellInfo instanceof CellInfoWcdma) {
            Log.d(getClass().getSimpleName(), "Found CellInfoWcdma");
            if (OsVersion.isAtLeast(Build.VERSION_CODES.JELLY_BEAN_MR2)) {
                CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                strength = cellInfoWcdma.getCellSignalStrength().getDbm();
                signalId = cellInfoWcdma.getCellIdentity().getCid();
                typeFound = true;
            }
        }
        if (typeFound) {
            CellSignal cellSignal = CellSignal.builder()
                    .timestamp(SystemClock.uptimeMillis())
                    .strengthInDb(strength)
                    .signalId(signalId)
                    .build();
            return Possible.of(cellSignal);
        } else {
            Log.w(getClass().getSimpleName(), "Unknown cellular info type: " + cellInfo.getClass().getName());
            return Possible.empty();
        }
    }
}
