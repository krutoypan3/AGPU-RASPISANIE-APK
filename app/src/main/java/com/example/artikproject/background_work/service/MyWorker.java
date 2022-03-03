package com.example.artikproject.background_work.service;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    public MyWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Result doWork() {
        new PlayService().get_group_db(getApplicationContext());
        return Result.success();
    }
}

