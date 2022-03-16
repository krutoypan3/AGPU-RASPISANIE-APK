package com.example.artikproject.layout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
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

import com.example.artikproject.background_work.rasp_show.GetRasp;
import com.example.artikproject.background_work.rasp_show.*;
import com.example.artikproject.background_work.OnSwipeTouchListener;
import com.example.artikproject.R;

import java.util.Random;

public class Raspisanie_show extends AppCompatActivity {
    public static ListView para_view;
    public static TextView mainText;
    public static TableLayout tableLayout;
    public static boolean refresh_on_off = false;
    public static boolean week_day_on_off = false;
    public static boolean refresh_successful = true;
    public static Button week_day_bt1;
    public static Button week_day_bt2;
    public static ImageView refresh_btn;
    public static ImageView refresh_btn_ficha;
    public static ImageView week_day_change_btn_size_up;
    public static ImageView week_day_change_btn_size_down;
    public static ConstraintLayout gesture_layout;
    public static ImageView week_day_change_btn;

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
        mainText = findViewById(R.id.main_text);
        para_view = findViewById(R.id.para_view);
        tableLayout = findViewById(R.id.table); // Инициализация таблицы
        refresh_btn_ficha = findViewById(R.id.refresh_btn_all); // Кнопка перекручивания расп (фича)
        refresh_btn = findViewById(R.id.refresh_btn); // Кнопка обновления расписания
        week_day_change_btn_size_up = findViewById(R.id.week_day_change_btn_size_up);
        week_day_change_btn_size_down = findViewById(R.id.week_day_change_btn_size_down);
        gesture_layout = findViewById(R.id.raspisanie_day); // Слой для отслеживания жестов
        RelativeLayout raspisanie_show_layout = findViewById(R.id.raspisanie_show); // Основной слой
        new Refresh_rasp_week_or_day_starter(getApplicationContext()).start();
        CheckBox mCheckBox = findViewById(R.id.checkBox); // Уведомление об обновлении расписания
        Cursor sss = MainActivity.sqLiteDatabase.rawQuery("SELECT r_group_code FROM rasp_update WHERE r_group_code = '" + MainActivity.selectedItem_id + "'", null);
        mCheckBox.setChecked(sss.getCount() != 0);
        mCheckBox.setOnClickListener(v -> {
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
        });

        // Кнопка увеличивающая размер текста в режиме недели
        week_day_change_btn_size_up.setOnClickListener(v -> new Week_show_resize().size_add());

        // Кнопка уменьшающая размер текста в режиме недели
        week_day_change_btn_size_down.setOnClickListener(v -> new Week_show_resize().size_dec());

        week_day_bt1 = findViewById(R.id.week_day_bt1);
        week_day_bt2 = findViewById(R.id.week_day_bt2);

        // Функция перехода на сайт с расписанием при нажатии на кнопку
        Button rasp_site = findViewById(R.id.rasp_site);
        rasp_site.setOnClickListener(v -> {
            rasp_site.setAnimation(MainActivity.animRotate_ok);
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.it-institut.ru/Raspisanie/SearchedRaspisanie?" +
                            "OwnerId=118&SearchId=" + MainActivity.selectedItem_id + "&" +
                            "SearchString=" + MainActivity.selectedItem + "&" +
                            "Type=" + MainActivity.selectedItem_type + "&" +
                            "WeekId=" + MainActivity.week_id)));
        });

        // Переход к предыдущему дню
        week_day_bt1.setOnClickListener(v -> swipe_day("Left"));

        // Переход к следующему дню
        week_day_bt2.setOnClickListener(v -> swipe_day("Right"));

        // Обновить расписание
        refresh_btn.setOnClickListener(v -> swipe_day("Bottom"));
        refresh_btn_ficha.setOnClickListener(v -> {
            int random_int = new Random().nextInt(3);
            switch (random_int){
                case 0: raspisanie_show_layout.startAnimation(MainActivity.animRotate_ok); break;
                case 1: raspisanie_show_layout.startAnimation(MainActivity.animScale); break;
                case 2: raspisanie_show_layout.startAnimation(MainActivity.animUehalVl); break;
                case 3: raspisanie_show_layout.startAnimation(MainActivity.animUehalVp); break;
            }
        });

        // Смена недельного режима и дневного
        week_day_change_btn = findViewById(R.id.week_day_change_btn);
        week_day_change_btn.setOnClickListener(v -> new Week_day_change(getApplicationContext()));

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
                        new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, getApplicationContext()).start();
                        new Refresh_rasp_week_or_day_starter(getApplicationContext()).start();
                    }
                    break;
            }
            if (MainActivity.isOnline(Raspisanie_show.this)) {
                new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, getApplicationContext()).start();
            }
            new Day_show(getApplicationContext());
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
            if(MainActivity.isOnline(Raspisanie_show.this)){
                new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, getApplicationContext()).start();
            }
            new Refresh_rasp_week_or_day_starter(getApplicationContext()).start();
        }
    }
 }
