package com.example.artikproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

public class GetRaspFromDB extends AsyncTask<String, String, String> {
    boolean start_activity;
    String r_selectedItem;
    String r_selectedItem_id;
    String r_selectedItem_type;
    int week_id_upd;
    Context context;
    private SQLiteDatabase sqLiteDatabaseS;
    String predmet_name;
    String predmet_prepod;
    String predmet_group;
    String predmet_podgroup;
    String predmet_aud;
    String predmet_razmer;
    String predmet_color;
    String predmet_distant;
    String predmet_time;
    String predmet_data_ned;
    String predmet_data_chi;

    public GetRaspFromDB(String r_selectedItem_id, String r_selectedItem_type, String r_selectedItem, int week_id_upd, Context context) {
        this.r_selectedItem_id = r_selectedItem_id;
        this.r_selectedItem_type = r_selectedItem_type;
        this.r_selectedItem = r_selectedItem;
        this.week_id_upd = week_id_upd;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        this.sqLiteDatabaseS = new DataBase(context).getWritableDatabase(); //Подключение к базе данных
        DateBase_Online dateBase_online = new DateBase_Online();
        return null;
    }
}
