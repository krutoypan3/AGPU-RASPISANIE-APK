package ru.agpu.artikproject.background_work.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, PlayService.class));
    }
}