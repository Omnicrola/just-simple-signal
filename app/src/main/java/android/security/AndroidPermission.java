package android.security;

import android.Manifest;

public enum AndroidPermission {
    ACCESS_COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION);


    private String systemString;

    private AndroidPermission(String systemString) {
        this.systemString = systemString;
    }

    @Override
    public String toString() {
        return this.systemString;
    }
}
