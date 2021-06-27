package com.cegep.sportify_admin;

import android.app.Application;
import androidx.multidex.MultiDexApplication;

public class SportifyAdminApp extends MultiDexApplication {

    public static Admin admin = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
