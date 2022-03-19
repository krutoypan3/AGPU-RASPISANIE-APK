package com.example.artikproject.background_work.service;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;


import com.example.artikproject.background_work.CheckInternetConnection;
import com.example.artikproject.background_work.datebase.DataBase_Local;
import com.example.artikproject.background_work.rasp_show.GetRasp;

import java.util.ArrayList;
import java.util.Date;
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WorkRequest uploadWorkRequest = new PeriodicWorkRequest.Builder(MyWorker.class, 5, TimeUnit.MINUTES).build();
        WorkManager.getInstance(getApplicationContext()).enqueue(uploadWorkRequest);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void get_group_db(Context context) {
        SQLiteDatabase sqLiteDatabaseS = new DataBase_Local(context).getWritableDatabase(); //Подключение к базе данных
        if (CheckInternetConnection.getState(context)) {
            Cursor r = sqLiteDatabaseS.rawQuery("SELECT r_group_code, r_selectedItem_type, r_selectedItem FROM rasp_update", null); // SELECT запрос
            ArrayList<String> r_group0 = new ArrayList<>();
            ArrayList<String> r_group1 = new ArrayList<>();
            ArrayList<String> r_group2 = new ArrayList<>();

            Date date1 = new Date();
            long date_ms = date1.getTime() + 10800000;
            int week_id_upd = (int) ((date_ms - 18489514000f) / 1000f / 60f / 60f / 24f / 7f); // Номер текущей недели
            r.moveToFirst();
            do {
                if (r.getCount() != 0) {
                    r_group0.add(r.getString(0));
                    r_group1.add(r.getString(1));
                    r_group2.add(r.getString(2));
                }
            } while (r.moveToNext());

            for (int i = 0; i < r_group0.size(); i++) {
                new GetRasp(r_group0.get(i), r_group1.get(i), r_group2.get(i), week_id_upd, context).start();
            }
        }
        sqLiteDatabaseS.close();
    }
}