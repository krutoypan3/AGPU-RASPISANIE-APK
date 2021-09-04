package com.example.artikproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.shapes.Shape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class raspisanie_show extends Activity {
    ListView para_view;
    public TextView mainText;
    public TableLayout tableLayout;
    Context context;
    public static boolean refresh_on_off = false;
    private static boolean week_day_on_off = false;
    public static boolean refresh_successful = true;
    public TextView[] qqty;
    public TableRow[] tableRows;
    public int table_size = 14;

    // Вызывается перед выходом из "полноценного" состояния.
    @Override
    public void onDestroy() {
        // Очистите все ресурсы. Это касается завершения работы
        // потоков, закрытия соединений с базой данных и т. д.
        super.onDestroy();
        week_day_on_off = false;
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
        ImageView refresh_btn_all = findViewById(R.id.refresh_btn_all); // Кнопка перекручивания расп (фича)
        ImageView refresh_btn = findViewById(R.id.refresh_btn); // Кнопка обновления расписания
        ConstraintLayout gesture_layout = findViewById(R.id.raspisanie_day); // Слой для отслеживания жестов
        RelativeLayout raspisanie_show_layout = findViewById(R.id.raspisanie_show); // Основной слой
        new refresh_day_show().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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


        ImageView week_day_change_btn_size_up = findViewById(R.id.week_day_change_btn_size_up);
        ImageView week_day_change_btn_size_down = findViewById(R.id.week_day_change_btn_size_down);

        week_day_change_btn_size_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table_size++;
                for (int i = 0; i < 60; i++){
                    qqty[i].setTextSize(table_size);
                }
                int fk = 0;
                int[] max_razmer = {0,0,0,0,0,0,0};
                for (int ff = 0; ff < 60; ff++){
                    if ((ff % 10) == 0 & ff != 0) {
                        fk++;
                    }
                    if (qqty[ff].getText().length() * table_size/4 > max_razmer[fk]){
                        max_razmer[fk] = (int) (qqty[ff].getText().length() * table_size/4);
                    }
                }
                fk = 0;
                for (int ff = 0; ff < 60; ff++){
                    if ((ff % 10) == 0 & ff != 0) {
                        fk++;
                    }
                    if (max_razmer[fk] < 120){
                        max_razmer[fk] = 120;
                    }
                    qqty[ff].setMinHeight(max_razmer[fk]);
                }
            }
        });
        week_day_change_btn_size_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table_size--;
                for (int i = 0; i < 60; i++){
                    qqty[i].setTextSize(table_size);
                }
                int fk = 0;
                int[] max_razmer = {0,0,0,0,0,0,0};
                for (int ff = 0; ff < 60; ff++){
                    if ((ff % 10) == 0 & ff != 0) {
                        fk++;
                    }
                    if (qqty[ff].getText().length() * table_size/4 > max_razmer[fk]){
                        max_razmer[fk] = (int) (qqty[ff].getText().length() * table_size/4);
                    }
                }
                fk = 0;
                for (int ff = 0; ff < 60; ff++){
                    if ((ff % 10) == 0 & ff != 0) {
                        fk++;
                    }
                    if (max_razmer[fk] < 120){
                        max_razmer[fk] = 120;
                    }
                    qqty[ff].setMinHeight(max_razmer[fk]);
                }
            }
        });


        Button week_day_bt1 = findViewById(R.id.week_day_bt1);
        Button week_day_bt2 = findViewById(R.id.week_day_bt2);

        // Функция перехода на сайт с расписанием при нажатии на кнопку
        Button rasp_site = findViewById(R.id.rasp_site);
        rasp_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rasp_site.setAnimation(MainActivity.animRotate_ok);
                Intent intent;
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.it-institut.ru/SearchString/Index/118"));
                startActivity(intent);
            }
        });

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
                    week_day_bt1.setClickable(true);
                    week_day_bt2.setClickable(true);
                    day_show(getApplicationContext());

                }
                else{
                    MainActivity.week_id -= 1;
                    if(MainActivity.isOnline(raspisanie_show.this)){
                        new GetRasp(false, MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, getApplicationContext()).execute();
                    }
                    week_show(raspisanie_show.this);
                    refresh_btn.setClickable(false);
                    week_day_bt1.setClickable(false);
                    week_day_bt2.setClickable(false);
                    refresh_on_off = true;
                    refresh_btn_all.setVisibility(View.VISIBLE);
                    refresh_btn.startAnimation(MainActivity.animRotate);
                    refresh_btn.setBackgroundResource(R.drawable.refresh_1);
                    new refresh_day_show().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
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
                    week_day_bt1.setClickable(true);
                    week_day_bt2.setClickable(true);
                    day_show(getApplicationContext());
                }
                else{
                    MainActivity.week_id += 1;
                    if(MainActivity.isOnline(raspisanie_show.this)){
                        new GetRasp(false, MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, getApplicationContext()).execute();
                    }
                    refresh_btn.setClickable(false);
                    week_day_bt1.setClickable(false);
                    week_day_bt2.setClickable(false);
                    refresh_on_off = true;
                    refresh_btn_all.setVisibility(View.VISIBLE);
                    refresh_btn.startAnimation(MainActivity.animRotate);
                    refresh_btn.setBackgroundResource(R.drawable.refresh_1);
                    new refresh_day_show().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    week_show(raspisanie_show.this);
                }
            }
        });

        // Обновить расписание
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!week_day_on_off){
                    refresh_btn.setClickable(false);
                    refresh_on_off = true;
                    refresh_btn_all.setVisibility(View.VISIBLE);
                    refresh_btn.startAnimation(MainActivity.animRotate);
                    refresh_btn.setBackgroundResource(R.drawable.refresh_1);
                    new GetRasp(false, MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, getApplicationContext()).execute();
                    new refresh_day_show().execute();

                }
                else{
                    refresh_btn_all.setVisibility(View.VISIBLE);
                    refresh_btn.setClickable(false);
                    refresh_on_off = true;
                    refresh_btn.startAnimation(MainActivity.animRotate);
                    refresh_btn.setBackgroundResource(R.drawable.refresh_1);
                    new GetRasp(false, MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, getApplicationContext()).execute();
                    new refresh_day_show().execute();
                }
            }
        });
        refresh_btn_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int random_int = new Random().nextInt(3);
                switch (random_int){
                    case 0:
                        raspisanie_show_layout.startAnimation(MainActivity.animRotate_ok);
                        break;
                    case 1:
                        raspisanie_show_layout.startAnimation(MainActivity.animScale);
                        break;
                    case 2:
                        raspisanie_show_layout.startAnimation(MainActivity.animUehalVl);
                        break;
                    case 3:
                        raspisanie_show_layout.startAnimation(MainActivity.animUehalVp);
                        break;
                }

            }
        });

        // Смена недельного режима и дневного
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
                    gesture_layout.setVisibility(View.INVISIBLE);
                    week_day_change_btn_size_up.setVisibility(View.VISIBLE);
                    week_day_change_btn_size_down.setVisibility(View.VISIBLE);
                }
                else{
                    week_day_change_btn.setImageResource(R.drawable.ic_baseline_date_range_24);
                    week_day_change_btn.setAnimation(MainActivity.animScale);
                    para_view.setVisibility(View.VISIBLE);
                    tableLayout.setVisibility(View.INVISIBLE);
                    week_day_on_off = false;
                    day_show(context);
                    gesture_layout.setVisibility(View.VISIBLE);
                    week_day_change_btn_size_up.setVisibility(View.INVISIBLE);
                    week_day_change_btn_size_down.setVisibility(View.INVISIBLE);
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
        gesture_layout.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeRight() {
                if (!week_day_on_off){
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
            }
            public void onSwipeLeft() {
                if (!week_day_on_off){
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
            }
            public void onSwipeBottom(){
                if (!refresh_on_off) {
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

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {

                    ImageView refresh_btn = findViewById(R.id.refresh_btn);
                    ImageView refresh_btn_all = findViewById(R.id.refresh_btn_all);
                    if (!week_day_on_off){
                        day_show(context);
                    }
                    else{
                        week_show(context);
                    }
                    if (refresh_successful) {
                        refresh_btn.setBackgroundResource(R.drawable.refresh_2);
                    }
                    else{
                        refresh_btn.setBackgroundResource(R.drawable.refresh_0);
                    }
                    refresh_btn.setClickable(true);
                    refresh_btn_all.setVisibility(View.INVISIBLE);
                    refresh_btn.startAnimation(MainActivity.animRotate_ok);
                    Button week_day_bt1 = findViewById(R.id.week_day_bt1);
                    Button week_day_bt2 = findViewById(R.id.week_day_bt2);
                    week_day_bt1.setClickable(true);
                    week_day_bt2.setClickable(true);
                }
            });
            return null;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void week_show(Context context){
        try{
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
                TextView empty_cell; // Новая пустая ячейка
                qqty = new TextView[60];
                tableRows = new TableRow[6];
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
                    qty.setTextSize(table_size);
                    qty.setPadding(5,5,5,5);
                    qty.setTextColor(ContextCompat.getColor(this, R.color.white));
                    qty.setBackgroundResource(R.drawable.table_granitsa_legenda);
                    qty.setGravity(Gravity.CENTER);
                    qty.setText(f.getString(0));
                    empty_cell = new TextView(this); // Новая пустая ячейка
                    empty_cell.setHeight(1);
                    empty_cell.setWidth(1);
                    timeRow.addView(empty_cell); // Добавление пустой ячейки в столбец
                    timeRow.addView(qty);
                }while(f.moveToNext());
                tableLayout.addView(timeRow);
                do{
                    if (Objects.equals(prev_time, r.getString(9))){
                        str += "\n";
                        tableRow.removeView(qty);
                        tableRow.removeView(empty_cell);
                    }
                    else{
                        str = "";
                        qty = new TextView(this); // Новая ячейка
                        qty.setMaxEms(10);
                        qty.setTextSize(table_size);
                        qty.setPadding(0,5,5,5);
                        qty.setTextColor(ContextCompat.getColor(this, R.color.black));
                        try {
                            qty.setBackgroundColor(Color.parseColor(r.getString(14)));
                        }
                        catch (Exception e){
                            qty.setBackgroundResource(R.color.gray);
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

                        tableRow = new TableRow(this); // Новый пустой столбец
                        empty_cell = new TextView(this); // Новая пустая ячейка
                        empty_cell.setHeight(1);
                        tableRow.addView(empty_cell); // Добавление пустой ячейки в столбец
                        tableLayout.addView(tableRow); // Добавление столбца в таблицу
                        tableRow = new TableRow(this); // Новый столбец
                        tableRow.setGravity(Gravity.CENTER_VERTICAL);
                        str = r.getString(10);
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
                    empty_cell = new TextView(this); // Новая пустая ячейка
                    empty_cell.setHeight(1);
                    empty_cell.setWidth(1);
                    tableRow.addView(empty_cell); // Добавление пустой ячейки в столбец
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
        catch (Exception ignored){ // Если недели нет в базе то ...
            mainText.setText("Вы слишком быстро листаете недели с расписанием...\n обновляю расписание...");
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
        } else {
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
