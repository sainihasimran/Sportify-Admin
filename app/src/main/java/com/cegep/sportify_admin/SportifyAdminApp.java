package com.cegep.sportify_admin;

import android.app.Application;
import androidx.multidex.MultiDexApplication;

import com.cegep.sportify_admin.Orders.Order;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.util.ArrayList;
import java.util.List;

public class SportifyAdminApp extends MultiDexApplication {

    public static final String CLIENT_FIREBASE = "CLIENT_FIREBASE";

    public static final String ADMIN_FIREBASE = "ADMIN_FIREBASE";

    public static Admin admin = null;

    public static List<Order> orders = new ArrayList<>();

    public static boolean isBuyMode = true;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseOptions clientOptions = new FirebaseOptions.Builder()
                .setProjectId("sportify-5484f")
                .setApplicationId("1:1012491882693:android:80fea00eb160e82d49a0ac")
                .setApiKey("AIzaSyAWywR_efXWmhsKe-bzGXimZAI4_pB0zsw")
                .setDatabaseUrl("https://sportify-5484f-default-rtdb.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(this, clientOptions, CLIENT_FIREBASE);
    }
}
