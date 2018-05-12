package com.omnicrola.justsimplesignal.tools;

public class OsVersion {

    public static boolean isAtLeast(int targetVersion) {
        return android.os.Build.VERSION.SDK_INT >= targetVersion;
    }
}
