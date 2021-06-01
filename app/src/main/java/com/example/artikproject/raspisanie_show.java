package com.example.artikproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class raspisanie_show extends Activity {
    ListView para_view;
    public static TextView mainText;
    Context context;
    public static boolean refresh_on_off = false;
    public static boolean refresh_successful = true;

    // Вызывается перед выходом из "полноценного" состояния.
    @Override
    public void onDestroy() {
        // Очистите все ресурсы. Это касается завершения работы
        // потоков, закрытия соединений с базой данных и т. д.
        super.onDestroy();
    }

    @Override
    public void finish(){
        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raspisanie_layout);
        context = getApplicationContext();
        mainText = findViewById(R.id.main_text);
        para_view = findViewById(R.id.para_view);
        new refresh_day_show().execute();
        CheckBox mCheckBox = (CheckBox)findViewById(R.id.checkBox);

        Cursor sss = MainActivity.sqLiteDatabase.rawQuery("SELECT r_group_code FROM rasp_update WHERE r_group_code = '" + MainActivity.selectedItem_id + "'", null);
        mCheckBox.setChecked(sss.getCount() != 0);

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

        // Возврат на главный экран
        ImageView back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_btn.startAnimation(MainActivity.animUehalVl);
                finish();
            }
        });

        Button week_day_bt1 = findViewById(R.id.week_day_bt1);
        Button week_day_bt2 = findViewById(R.id.week_day_bt2);

        // Переход к предыдущему дню
        week_day_bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                week_day_bt1.setAnimation(MainActivity.animUehalVl);
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
                day_show(getApplicationContext());
                week_day_bt1.setClickable(true);
                week_day_bt2.setClickable(true);
            }

        });

        // Переход к следующему дню
        week_day_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                week_day_bt2.setAnimation(MainActivity.animUehalVp);
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
                day_show(getApplicationContext());
                week_day_bt1.setClickable(true);
                week_day_bt2.setClickable(true);
            }
        });

        // Обновить расписание
        ImageView refresh_btn = findViewById(R.id.refresh_btn);
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh_btn.setClickable(false);
                refresh_on_off = true;
                refresh_btn.startAnimation(MainActivity.animRotate);
                refresh_btn.setBackgroundResource(R.drawable.refresh_1);
                new GetRasp(false, MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, getApplicationContext()).execute();
                new refresh_day_show().execute();
            }
        });

        // отслеживание жестов
        ListView para_view = findViewById(R.id.para_view);
        para_view.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeRight() {
                week_day_bt1.setAnimation(MainActivity.animUehalVl);
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
                day_show(getApplicationContext());
                week_day_bt1.setClickable(true);
                week_day_bt2.setClickable(true);
            }
            public void onSwipeLeft() {
                week_day_bt2.setAnimation(MainActivity.animUehalVp);
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
                day_show(getApplicationContext());
                week_day_bt1.setClickable(true);
                week_day_bt2.setClickable(true);
            }
        });

        // отслеживание жестов
        RelativeLayout raspisanie_show = findViewById(R.id.raspisanie_show);
        raspisanie_show.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeRight() {
                week_day_bt1.setAnimation(MainActivity.animUehalVl);
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
                day_show(getApplicationContext());
                week_day_bt1.setClickable(true);
                week_day_bt2.setClickable(true);
            }
            public void onSwipeLeft() {
                week_day_bt2.setAnimation(MainActivity.animUehalVp);
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
                day_show(getApplicationContext());
                week_day_bt1.setClickable(true);
                week_day_bt2.setClickable(true);
            }
            public void onSwipeBottom(){
                if (!refresh_on_off){
                    refresh_btn.setClickable(false);
                    refresh_on_off = true;
                    refresh_btn.startAnimation(MainActivity.animRotate);
                    refresh_btn.setBackgroundResource(R.drawable.refresh_1);
                    new GetRasp(false, MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, getApplicationContext()).execute();
                    new refresh_day_show().execute();
                }
            }
        });
    }

    class refresh_day_show extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            while (refresh_on_off){
                try {
                  TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
            }
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    ImageView refresh_btn = findViewById(R.id.refresh_btn);
                    day_show(context);
                    if (refresh_successful) {
                        refresh_btn.setBackgroundResource(R.drawable.refresh_2);
                    }
                    else{
                        refresh_btn.setBackgroundResource(R.drawable.refresh_0);
                    }
                    refresh_btn.setClickable(true);
                    refresh_btn.startAnimation(MainActivity.animRotate_ok);
                }
            });
            return null;
        }
    }
    protected void day_show(Context context){
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
        ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, group_list);
        para_view.setAdapter(adapter);
    }

 }
