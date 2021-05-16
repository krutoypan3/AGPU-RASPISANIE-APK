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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class raspisanie_show extends Activity {
    ListView para_view;
    TextView mainText;

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
        para_view = findViewById(R.id.para_view);
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

        ListView para_view = findViewById(R.id.para_view);
        para_view.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeRight() {
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
            public void onSwipeLeft() {
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


        RelativeLayout raspisanie_show = findViewById(R.id.raspisanie_show);
        raspisanie_show.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeRight() {
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
            public void onSwipeLeft() {
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
        List<String> group_list = new ArrayList<>();
        Cursor r = MainActivity.sqLiteDatabase.rawQuery("SELECT * FROM rasp_test1 WHERE " +
                "r_group_code = " + MainActivity.selectedItem_id + " AND " +
                "r_week_number = " + MainActivity.week_id + " AND " +
                "r_week_day = " + MainActivity.week_day + " ORDER BY r_para_number", null);
        if (r.getCount()==0) {// Если даной недели нет в базе
//            mainText.setText("\uD83E\uDD7A"); // Тут типа грустный смайл
//            addText.setText("Для просмотра текущего дня необходимо подключение к интернету...");
        }
        else{
            String str;
            r.moveToFirst();
            mainText.setText(r.getString(10) + " " + r.getString(11));
            do{
                str = "";
                if (r.getString(4) != null){
                    str += r.getString(9) + "\n";
                    str += r.getString(4) + "\n";
                    if (r.getString(5) != null) str += r.getString(5) + "\n";
                    if (r.getString(6) != null) str += r.getString(6) + "\n";
                    if (r.getString(7) != null) str += r.getString(7) + "\n";
                    if (r.getString(8) != null) str += r.getString(8);
                    group_list.add(str);
                }
            }while(r.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, group_list);
        para_view.setAdapter(adapter);
    }

 }
