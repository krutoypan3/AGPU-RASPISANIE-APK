package com.example.artikproject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class GetRaspOnline extends AsyncTask<String, String, String> {
    String r_selectedItem_id;
    int week_id_upd;
    Context context;


    public GetRaspOnline(String r_selectedItem_id, int week_id_upd, Context context) {
        this.r_selectedItem_id = r_selectedItem_id;
        this.week_id_upd = week_id_upd;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected String doInBackground(String... strings) {
        DateBaseOnline dateBase_online = new DateBaseOnline(); // Подключение онлайн бд
        raspisanie_show.refresh_on_off = false;
        dateBase_online.GetGroupRasp(r_selectedItem_id, week_id_upd, context); // Запрос на получение расписания
        raspisanie_show.refresh_successful = true;
        return null;
    }
}