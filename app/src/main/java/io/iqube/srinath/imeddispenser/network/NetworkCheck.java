package io.iqube.srinath.imeddispenser.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkCheck extends BroadcastReceiver {


    public static boolean Connected = false;

    public NetworkCheck() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                Connected = true;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                Connected = true;
        } else
            Connected = false;
        if (Connected) {

        }

    }
}