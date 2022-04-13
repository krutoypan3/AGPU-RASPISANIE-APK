package com.example.artikproject.background_work.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;


import java.util.concurrent.TimeUnit;

public class PlayService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PeriodicWorkRequest uploadWorkRequest = new PeriodicWorkRequest.Builder(MyWorker.class, 5, TimeUnit.MINUTES).build();
        WorkManager.getInstance(getApplicationContext()).enqueue(uploadWorkRequest);
        System.out.println("Сервис номер " + startId + " был запущен");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}