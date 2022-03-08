package com.example.artikproject.layout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.artikproject.background_work.GetRasp;
import com.example.artikproject.background_work.rasp_show.*;
import com.example.artikproject.background_work.OnSwipeTouchListener;
import com.example.artikproject.R;

import java.util.Random;

public class raspisanie_show extends AppCompatActivity {
    public static ListView para_view;
    public static TextView mainText;
    public static TableLayout tableLayout;
    Context context;
    public static boolean refresh_on_off = false;
    public static boolean week_day_on_off = false;
    public static boolean refresh_successful = true;
    public static Button week_day_bt1;
    public static Button week_day_bt2;
    public static ImageView refresh_btn;
    public static ImageView refresh_btn_ficha;

    // Вызывается перед выходом из "полноценного" состояния.
    @Override
    public void onDestroy() {
        // Очистите все ресурсы. Это касается завершения работы
        // потоков, закрытия соединений с базой данных и т. д.
        super.onDestroy();
        week_day_on_off = false;
    }

    @Override
    public void finish(){ super.finish(); }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raspisanie_layout);
        context = getApplicationContext();
        mainText = findViewById(R.id.main_text);
        para_view = findViewById(R.id.para_view);
        tableLayout = findViewById(R.id.table); // Инициализация таблицы
        refresh_btn_ficha = findViewById(R.id.refresh_btn_all); // Кнопка перекручивания расп (фича)
        refresh_btn = findViewById(R.id.refresh_btn); // Кнопка обновления расписания
        ConstraintLayout gesture_layout = findViewById(R.id.raspisanie_day); // Слой для отслеживания жестов
        RelativeLayout raspisanie_show_layout = findViewById(R.id.raspisanie_show); // Основной слой
        new refresh_rasp_week_or_day_starter(context).start();
        CheckBox mCheckBox = (CheckBox)findViewById(R.id.checkBox); // Уведомление об обновлении расписания
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
                    mCheckBox.setTextColor(Color.GRAY);
                    MainActivity.sqLiteDatabase.delete("rasp_update", "r_group_code = '" + MainActivity.selectedItem_id + "'", null);
                }
            }
        });

        ImageView week_day_change_btn_size_up = findViewById(R.id.week_day_change_btn_size_up);
        ImageView week_day_change_btn_size_down = findViewById(R.id.week_day_change_btn_size_down);

        // Кнопка увеличивающая размер текста в режиме недели
        week_day_change_btn_size_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new week_show_resize().size_add();
            }
        });

        // Кнопка уменьшающая размер текста в режиме недели
        week_day_change_btn_size_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new week_show_resize().size_dec();
            }
        });

        week_day_bt1 = findViewById(R.id.week_day_bt1);
        week_day_bt2 = findViewById(R.id.week_day_bt2);

        // Функция перехода на сайт с расписанием при нажатии на кнопку
        Button rasp_site = findViewById(R.id.rasp_site);
        rasp_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rasp_site.setAnimation(MainActivity.animRotate_ok);
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.it-institut.ru/Raspisanie/SearchedRaspisanie?" +
                                "OwnerId=118&SearchId=" + MainActivity.selectedItem_id + "&" +
                                "SearchString=" + MainActivity.selectedItem + "&" +
                                "Type=" + MainActivity.selectedItem_type + "&" +
                                "WeekId=" + MainActivity.week_id)));
            }
        });

        // Переход к предыдущему дню
        week_day_bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { swipe_day("Left"); }
        });

        // Переход к следующему дню
        week_day_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { swipe_day("Right"); }
        });

        // Обновить расписание
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { swipe_day("Bottom"); }
        });
        refresh_btn_ficha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int random_int = new Random().nextInt(3);
                switch (random_int){
                    case 0: raspisanie_show_layout.startAnimation(MainActivity.animRotate_ok); break;
                    case 1: raspisanie_show_layout.startAnimation(MainActivity.animScale); break;
                    case 2: raspisanie_show_layout.startAnimation(MainActivity.animUehalVl); break;
                    case 3: raspisanie_show_layout.startAnimation(MainActivity.animUehalVp); break;
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
                    new week_show(context);
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
                    new day_show(context);
                    gesture_layout.setVisibility(View.VISIBLE);
                    week_day_change_btn_size_up.setVisibility(View.INVISIBLE);
                    week_day_change_btn_size_down.setVisibility(View.INVISIBLE);
                }
            }
        });

        // отслеживание жестов
        ListView para_view = findViewById(R.id.para_view);

        para_view.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeRight() { swipe_day("Left"); }
            public void onSwipeLeft() { swipe_day("Right"); }
        });


        // отслеживание жестов
        gesture_layout.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeRight() { swipe_day("Left"); }
            public void onSwipeLeft() { swipe_day("Right"); }
            public void onSwipeBottom(){ swipe_day("Bottom"); }
        });
    }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void swipe_day(String direction){
        week_day_bt1.setClickable(false);
        week_day_bt2.setClickable(false);
        if (!week_day_on_off) {
            switch (direction) {
                case "Left":
                    week_day_bt1.setAnimation(MainActivity.animUehalVl);
                    MainActivity.week_day -= 1;
                    if (MainActivity.week_day == -1) { // Если будет воскресенье, то будет показана суббота
                        MainActivity.week_day = 5;
                        MainActivity.week_id -= 1;
                    }
                    break;
                case "Right":
                    week_day_bt2.setAnimation(MainActivity.animUehalVp);
                    MainActivity.week_day += 1;
                    if (MainActivity.week_day == 6) { // Если будет воскресенье, то будет показана суббота
                        MainActivity.week_day = 0;
                        MainActivity.week_id += 1;
                    }
                    break;
                case "Bottom":
                    if (!refresh_on_off) {
                        refresh_btn.setClickable(false);
                        refresh_btn_ficha.setVisibility(View.VISIBLE);
                        refresh_on_off = true;
                        refresh_btn.startAnimation(MainActivity.animRotate);
                        refresh_btn.setBackgroundResource(R.drawable.refresh_1);
                        new GetRasp(false, MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, getApplicationContext()).start();
                        new refresh_rasp_week_or_day_starter(context).start();
                    }
                    break;
            }
            if (MainActivity.isOnline(raspisanie_show.this)) {
                new GetRasp(false, MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, getApplicationContext()).start();
            }
            new day_show(getApplicationContext());
            week_day_bt1.setClickable(true);
            week_day_bt2.setClickable(true);
        } else{
            refresh_on_off = true;
            refresh_btn.setClickable(false);
            refresh_btn_ficha.setVisibility(View.VISIBLE);
            refresh_btn.startAnimation(MainActivity.animRotate);
            refresh_btn.setBackgroundResource(R.drawable.refresh_1);
            switch (direction){
                case "Left": MainActivity.week_id -= 1; break;
                case "Right": MainActivity.week_id += 1; break;
            }
            if(MainActivity.isOnline(raspisanie_show.this)){
                new GetRasp(false, MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, getApplicationContext()).start();
            }
            new refresh_rasp_week_or_day_starter(context).start();
            new week_show(raspisanie_show.this);
        }
    }
 }
