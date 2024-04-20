package com.example.ecomerceapp1.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

public class InternetReceiver extends BroadcastReceiver {
    //back to android manifest to assign an event
    @Override
    public void onReceive(Context context, Intent intent) {
       String status = CheckInternet.getNetworkInfo(context);
        if(status.equals("connected")){
            Toast.makeText(context.getApplicationContext(), "Connected to Internet", Toast.LENGTH_SHORT).show();
        }else if(status.equals("disconnected")){
            Toast.makeText(context.getApplicationContext(), "Disconnected to Internet", Toast.LENGTH_SHORT).show();
        }
    }

}
