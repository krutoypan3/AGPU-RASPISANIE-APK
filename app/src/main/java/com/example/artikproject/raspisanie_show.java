package com.example.artikproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class raspisanie_show extends Activity {
    TextView mainText;
    TextView addText;

    // Вызывается перед выходом из "полноценного" состояния.
    @Override
    public void onDestroy(){
        // Очистите все ресурсы. Это касается завершения работы
        // потоков, закрытия соединений с базой данных и т. д.
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raspisanie_layout);
        mainText = findViewById(R.id.main_text);
        addText = findViewById(R.id.add_text);
        day_show();
        if(MainActivity.isOnline(raspisanie_show.this)) {
            new getraspweek().execute("");
        }
        CheckBox mCheckBox;
        mCheckBox = (CheckBox)findViewById(R.id.checkBox);

        Cursor sss = MainActivity.sqLiteDatabase.rawQuery("SELECT r_group_code FROM rasp_update WHERE r_group_code = '" + MainActivity.selectedItem_id + "'", null);
        if (sss.getCount()==0) mCheckBox.setChecked(false);
        else mCheckBox.setChecked(true);

        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckBox.isChecked()) {
                    mCheckBox.setTextColor(Color.GREEN);
                    ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
                    rowValues.put("r_group_code", MainActivity.selectedItem_id);
                    rowValues.put("r_selectedItem_type", MainActivity.selectedItem_type);
                    rowValues.put("r_selectedItem", MainActivity.selectedItem);
                    MainActivity.sqLiteDatabase.insert("rasp_update", null, rowValues);
                }
                else{
                    mCheckBox.setTextColor(Color.BLACK);
                    MainActivity.sqLiteDatabase.delete("rasp_update", "r_group_code = '" + MainActivity.selectedItem_id + "'", null);
                }
            }
        });

        Button week_day_bt1;
        Button week_day_bt2;
        week_day_bt1 = findViewById(R.id.week_day_bt1);
        week_day_bt2 = findViewById(R.id.week_day_bt2);
        week_day_bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                week_day_bt1.setClickable(false);
                week_day_bt2.setClickable(false);
                MainActivity.week_day -= 1;
                if (MainActivity.week_day == -1){ // Если будет воскресенье, то будет показана суббота
                    MainActivity.week_day = 5;
                    MainActivity.week_id -= 1;
                    if(MainActivity.isOnline(raspisanie_show.this)){
                        new getraspweek().execute("");
                    }
                }
                day_show();
                week_day_bt1.setClickable(true);
                week_day_bt2.setClickable(true);
            }

        });
        week_day_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                week_day_bt1.setClickable(false);
                week_day_bt2.setClickable(false);
                MainActivity.week_day += 1;
                if (MainActivity.week_day == 6){ // Если будет воскресенье, то будет показана суббота
                    MainActivity.week_day = 0;
                    MainActivity.week_id += 1;
                    if(MainActivity.isOnline(raspisanie_show.this)){
                        new getraspweek().execute("");
                    }
                }
                day_show();
                week_day_bt1.setClickable(true);
                week_day_bt2.setClickable(true);
            }
        });
    }
    protected void day_show(){
        Cursor r = MainActivity.sqLiteDatabase.rawQuery("SELECT * FROM rasp_test1 WHERE " +
                "r_group_code = " + MainActivity.selectedItem_id + " AND " +
                "r_week_number = " + MainActivity.week_id + " AND " +
                "r_week_day = " + MainActivity.week_day + " ORDER BY r_para_number", null);
        if (r.getCount()==0) {// Если даной недели нет в базе
            mainText.setText("\uD83E\uDD7A");
            addText.setText("Для просмотра текущего дня необходимо подключение к интернету...");
        }
        else{
            String str = "";
            r.moveToFirst();
            mainText.setText(r.getString(10) + " " + r.getString(11));
            do{
                if (r.getString(4) != null){
                    str += r.getString(9) + "\n";
                    str += r.getString(4) + "\n";
                    if (r.getString(5) != null) str += r.getString(5) + "\n";
                    if (r.getString(6) != null) str += r.getString(6) + "\n";
                    if (r.getString(7) != null) str += r.getString(7) + "\n";
                    if (r.getString(8) != null) str += r.getString(8) + "\n";
                    str += "\n";
                }
            }while(r.moveToNext());
            addText.setText(str);
        }
    }

    public class getraspweek extends AsyncTask<String, String, String> { // INTERNET ONLY

        @Override
        protected String doInBackground(String... strings) {
            for (int ff = -1; ff < 2; ff++) {
                String urlq = "https://www.it-institut.ru/Raspisanie/SearchedRaspisanie?OwnerId=118&SearchId=" + MainActivity.selectedItem_id + "&SearchString=" + MainActivity.selectedItem + "&Type="+ MainActivity.selectedItem_type + "&WeekId=" + (MainActivity.week_id + ff);
                Document doc = null;
                try {
                    doc = Jsoup.connect(urlq).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert doc != null;
                List<String[]> days = new ArrayList<>();
                for (int i = 1; i < 7; i++) {
                    days.add(doc.select("tbody").toString().split("th scope")[i].split("td colspan="));
                }
                String[] day_data_razmer = new String[7];
                String[] day_data_time = new String[7];
                String[] dataaa = doc.select("thead").toString().split("colspan=\"");
                for (int i = 1; i < 8; i++) {
                    day_data_razmer[i-1] = (dataaa[i].split("\"")[0]);
                    day_data_time[i-1] = (dataaa[i].split(">")[2].split("<")[0]);
                }
                String[][] day;
                day = days.toArray(new String[0][0]);
                for (int i = 0; i < 6; i++) {
                    boolean prohod = false;
                    int schet = 0;
                    String predmet_data_ned = day[i][0].split("row\">")[1].split("<br>")[0];
                    String predmet_data_chi = day[i][0].split("<br>")[1].split("</th>")[0];
                    for (int j = 0; j < 10; j++) {

                        String predmet_name = null;
                        String predmet_prepod = null;
                        String predmet_group = null;
                        String predmet_podgroup = null;
                        String predmet_razmer = null;
                        try {
                            predmet_razmer = day[i][j].split("\"")[1];
                            predmet_name = day[i][j].split("<span>")[1].split("</span>")[0];
                            predmet_prepod = day[i][j].split("<span>")[2].split("</span>")[0];
                            predmet_group = day[i][j].split("<span>")[3].split("</span>")[0];
                            predmet_podgroup = day[i][j].split("<span>")[4].split("</span>")[0];
                        } catch (Exception ignored) {
                        }
                        String predmet_time = null;
                        if((j>0) && (schet < 7)){
                            predmet_time = day_data_time[schet];
                            if(day_data_razmer[schet].equals(predmet_razmer)){
                                if (prohod){
                                    prohod = false;
                                }
                                else{
                                    schet++;
                                }
                            }
                            else{
                                if (!prohod){
                                    prohod = true;
                                }
                                else{
                                    schet++;
                                    prohod = false;
                                }
                            }
                        }
                        Cursor r = MainActivity.sqLiteDatabase.rawQuery("SELECT * FROM rasp_test1 WHERE r_group_code = " + MainActivity.selectedItem_id + " AND r_week_number = " + (MainActivity.week_id + ff) + " AND r_week_day = " + i + " AND r_para_number = " + j, null); // SELECT запрос
                        if (r.getCount() == 0) {// Если даной недели нет в базе
                            ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
                            rowValues.put("r_group_code", MainActivity.selectedItem_id);
                            rowValues.put("r_week_day", i);
                            rowValues.put("r_week_number", (MainActivity.week_id + ff));
                            rowValues.put("r_para_number", j);
                            rowValues.put("r_name", predmet_name);
                            rowValues.put("r_prepod", predmet_prepod);
                            rowValues.put("r_group", predmet_group);
                            rowValues.put("r_podgroup", predmet_podgroup);
                            rowValues.put("r_aud", "");
                            rowValues.put("r_razmer", predmet_time);
                            rowValues.put("r_week_day_name", predmet_data_ned);
                            rowValues.put("r_week_day_date", predmet_data_chi);
                            rowValues.put("r_last_update", new Date().getTime());
                            MainActivity.sqLiteDatabase.insert("rasp_test1", null, rowValues); // Вставка строки в базу данных
                        }
                    }
                }}
            return null;
        }
    }

 }
