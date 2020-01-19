package com.example.foodcalc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper nH=new NotificationHelper(context);
        NotificationCompat.Builder nb= nH.getChanne11Notification();
        nH.getManager().notify(1,nb.build());
    }
}
