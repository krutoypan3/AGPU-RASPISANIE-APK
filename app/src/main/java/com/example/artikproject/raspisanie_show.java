package com.example.artikproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class raspisanie_show extends Activity {
    ListView para_view;
    public TextView mainText;
    public TableLayout tableLayout;
    Context context;
    public static boolean refresh_on_off = false;
    public static boolean week_day_on_off = false;
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raspisanie_layout);
        context = getApplicationContext();
        mainText = findViewById(R.id.main_text);
        para_view = findViewById(R.id.para_view);
        tableLayout = findViewById(R.id.table); // Инициализация таблицы
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
                if (!week_day_on_off){
                    MainActivity.week_day -= 1;
                    if (MainActivity.week_day == -1){ // Если будет воскресенье, то будет показана суббота
                        MainActivity.week_day = 5;
                        MainActivity.week_id -= 1;
                        if(MainActivity.isOnline(raspisanie_show.this)){
                            new getraspweek().execute("");
                        }
                    }
                    day_show(getApplicationContext());

                }
                else{
                    MainActivity.week_id -= 1;
                    if(MainActivity.isOnline(raspisanie_show.this)){
                        new getraspweek().execute("");
                    }
                    week_show(raspisanie_show.this);
                }
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
                if (!week_day_on_off){
                    MainActivity.week_day += 1;
                    if (MainActivity.week_day == 6){ // Если будет воскресенье, то будет показана суббота
                        MainActivity.week_day = 0;
                        MainActivity.week_id += 1;
                        if(MainActivity.isOnline(raspisanie_show.this)){
                            new getraspweek().execute("");
                        }
                    }
                    day_show(getApplicationContext());
                }
                else{
                    MainActivity.week_id += 1;
                    if(MainActivity.isOnline(raspisanie_show.this)){
                        new getraspweek().execute("");
                    }
                    week_show(raspisanie_show.this);
                }
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

        // Обновить расписание
        ImageView week_day_change_btn = findViewById(R.id.week_day_change_btn);
        week_day_change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!week_day_on_off){
                    week_day_change_btn.setImageResource(R.drawable.ic_baseline_today_24);
                    week_day_change_btn.setAnimation(MainActivity.animScale);
                    week_show(context);
                    para_view.setVisibility(View.INVISIBLE);
                    tableLayout.setVisibility(View.VISIBLE);
                    week_day_on_off = true;
                }
                else{
                    week_day_change_btn.setImageResource(R.drawable.ic_baseline_date_range_24);
                    week_day_change_btn.setAnimation(MainActivity.animScale);
                    para_view.setVisibility(View.VISIBLE);
                    tableLayout.setVisibility(View.INVISIBLE);
                    week_day_on_off = false;
                }
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void week_show(Context context){
        Cursor r = MainActivity.sqLiteDatabase.rawQuery("SELECT * FROM rasp_test1 WHERE " +
                "r_group_code = " + MainActivity.selectedItem_id + " AND " +
                "r_week_number = " + MainActivity.week_id + " ORDER BY r_week_day, r_para_number", null);
        Cursor f = MainActivity.sqLiteDatabase.rawQuery("SELECT DISTINCT r_razmer FROM rasp_test1 WHERE " +
                "r_group_code = " + MainActivity.selectedItem_id + " AND " +
                "r_week_number = " + MainActivity.week_id + " ORDER BY r_week_day, r_para_number", null);

        if (r.getCount()==0) {// Если даной недели нет в базе
//            mainText.setText("\uD83E\uDD7A"); // Тут типа грустный смайл
//            addText.setText("Для просмотра текущего дня необходимо подключение к интернету...");
        }
        else{
            String prev_time = "";
            tableLayout.removeAllViews();
            TableRow tableRow = new TableRow(this); // Новый столбец
            String str = "";
            TextView qty; // Новая ячейка
            TextView[] qqty = new TextView[60];
            TableRow[] tableRows = new TableRow[6];
            int[] max_razmer = {0,0,0,0,0,0,0};
            int ff = 0;
            int fk = 0;
            r.moveToFirst();
            mainText.setText(r.getString(10) + " " + r.getString(11));
            f.moveToFirst();
            TableRow timeRow = new TableRow(this); // Новый столбец
            do{
                qty = new TextView(this); // Новая ячейка
                qty.setMaxEms(10);
                qty.setTextSize(14);
                qty.setPadding(5,5,5,5);
                qty.setTextColor(ContextCompat.getColor(this, R.color.white));
                qty.setBackgroundResource(R.drawable.table_granitsa_legenda);
                qty.setGravity(Gravity.CENTER);
                qty.setText(f.getString(0));
                timeRow.addView(qty);
            }while(f.moveToNext());
            tableLayout.addView(timeRow);
            do{
                if (Objects.equals(prev_time, r.getString(9))){
                    str += "\n";
                    tableRow.removeView(qty);
                }
                else{
                    str = "123123123\n";
                    qty = new TextView(this); // Новая ячейка
                    qty.setMaxEms(10);
                    qty.setTextSize(14);
                    qty.setPadding(0,5,5,5);
                    qty.setTextColor(ContextCompat.getColor(this, R.color.black));
                    try {
                        qty.setBackgroundColor(Color.parseColor(r.getString(14)));
                    }
                    catch (Exception e){
                        qty.setBackgroundResource(R.drawable.table_granitsa);
                    }
                    qty.setGravity(Gravity.CENTER);
                }
                prev_time = r.getString(9);
                if (r.getString(4) != null){
                    str += r.getString(4) + "\n";
                    if (r.getString(5) != null) str += r.getString(5) + "\n";
                    if (r.getString(6) != null) str += r.getString(6) + "\n";
                    if (r.getString(7) != null) str += r.getString(7) + "\n";
                    if (r.getString(8) != null) str += r.getString(8);
                }
                if (r.getString(3).equals("0")){
                    qty.setMaxEms(14);
                    tableRow = new TableRow(this); // Новый столбец
                    tableRow.setGravity(Gravity.CENTER_VERTICAL);
                    str = r.getString(10);
//                    switch (str) {
//                        case  ("Понедельник"):
//                            str = "ПН";
//                            break;
//                        case ("Вторник"):
//                            str = "ВТ";
//                            break;
//                        case ("Среда"):
//                            str = "СР";
//                            break;
//                        case  ("Четверг"):
//                            str = "ЧТ";
//                            break;
//                        case ("Пятница"):
//                            str = "ПТ";
//                            break;
//                        case ("Суббота"):
//                            str = "СБ";
//                            break;
//                        default:
//                            break;
//                    }
                    str += "\n" + (r.getString(11));
                    tableLayout.addView(tableRow); // Добавление столбца в таблицу
                    tableRows[fk] = tableRow;
                    fk++;
                    try {
                        qty.setBackgroundColor(Color.parseColor(r.getString(14)));
                    }
                    catch (Exception e){
                        qty.setBackgroundResource(R.drawable.table_granitsa_legenda);
                    }
                    qty.setTextColor(ContextCompat.getColor(this, R.color.white));
                }
                qty.setText(str);
                tableRow.addView(qty); // Добавление ячейки в столбец
                qqty[ff] = qty;
                ff++;
            }while(r.moveToNext());
            tableRow.removeView(qty); // Удаление последней ячейки(хз что за ячейка (мусор какой-то))
            fk = 0;
            for (ff = 0; ff < 60; ff++){
                if ((ff % 10) == 0 & ff != 0) {
                    fk++;
                }
                if (qqty[ff].getText().length() * 4 > max_razmer[fk]){
                    max_razmer[fk] = (int) (qqty[ff].getText().length() * 4);
                }
            }
            fk = 0;
            for (ff = 0; ff < 60; ff++){
                if ((ff % 10) == 0 & ff != 0) {
                    fk++;
                }
                if (max_razmer[fk] < 120){
                    max_razmer[fk] = 120;
                }
                qqty[ff].setMinHeight(max_razmer[fk]);
            }

            mainText.setText("");
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
