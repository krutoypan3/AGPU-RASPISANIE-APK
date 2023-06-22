package ru.agpu.artikproject.background_work.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class BootBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("onReceive", "Обн.: " + intent.getAction());
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, PlayService.class));
            } else {
                context.startService(new Intent(context, PlayService.class));
            }
        } catch (Exception e) {
            Log.e("BootBroadcast", "onReceive error");
            e.printStackTrace();
        }
    }
}