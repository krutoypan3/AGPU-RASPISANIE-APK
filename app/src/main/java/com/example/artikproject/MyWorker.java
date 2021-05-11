package com.example.artikproject;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class MyWorker extends Worker {

    public MyWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Result doWork() {

        // Do the work here--in this case, upload the images.
        new PlayService().get_group_db(getApplicationContext());
        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }
}

