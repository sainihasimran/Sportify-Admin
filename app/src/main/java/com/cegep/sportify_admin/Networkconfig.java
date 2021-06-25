package com.cegep.sportify_admin;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.net.InetAddress;

public class Networkconfig {

        public boolean checkNetwork(Activity activity)
        {
            ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

            if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected())
            {
                return true;
            }else
            {
                return false;
            }
        }

        public boolean checkInternet()
        {
            try
            {
                InetAddress iAdress = InetAddress.getByName("http://google.com");

                return !iAdress.equals("");
            }catch (Exception e)
            {
                Log.d("NetworkCheck", e.getMessage());
                return false;
            }
        }
    }

