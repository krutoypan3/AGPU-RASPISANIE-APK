package com.example.artikproject.layout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import com.example.artikproject.R;
import com.example.artikproject.background_work.CustomBackground;
import com.example.artikproject.background_work.OnSwipeTouchListener;
import com.example.artikproject.background_work.rasp_show.CheckGroupRaspUpdate;
import com.example.artikproject.background_work.rasp_show.ListViewDayPara_Listener;
import com.example.artikproject.background_work.rasp_show.Refresh_rasp_week_or_day_starter;
import com.example.artikproject.background_work.rasp_show.Swipe_rasp;
import com.example.artikproject.background_work.rasp_show.Week_day_change;
import com.example.artikproject.background_work.rasp_show.Week_show_resize;
import com.example.artikproject.background_work.settings_layout.Ficha_achievements;

import java.util.Random;

public class Raspisanie_show extends AppCompatActivity {
    public static ListView day_para_view;
    public static TextView mainText;
    public static TableLayout week_para_view;
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

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raspisanie_layout);

        // Установка нового фона и затемнителя | Должно быть после setContentView
        findViewById(R.id.raspisanie_show).setBackground(CustomBackground.getBackground(getApplicationContext()));
        findViewById(R.id.background_darker).setBackgroundColor(CustomBackground.getBackgroundDarker(getApplicationContext()));

        mainText = findViewById(R.id.main_text); // Основной текст в центре
        day_para_view = findViewById(R.id.day_para_view); // Инициализация списка с расписанием
        week_para_view = findViewById(R.id.week_para_view); // Инициализация таблицы с расписанием
        week_day_bt1 = findViewById(R.id.week_day_bt1); // Кнопка перехода к предыдущему дню
        week_day_bt2 = findViewById(R.id.week_day_bt2); // Кнопка перехода к следующему дню
        week_day_change_btn = findViewById(R.id.week_day_change_btn); // Кнопка смены режима просмотра расписания [День / Неделя]
        refresh_btn_ficha = findViewById(R.id.refresh_btn_all); // Кнопка перекручивания расп (фича)
        refresh_btn = findViewById(R.id.refresh_btn); // Кнопка обновления расписания
        week_day_change_btn_size_up = findViewById(R.id.week_day_change_btn_size_up); // Кнопка увеличения размера текста в недельном режиме
        week_day_change_btn_size_down = findViewById(R.id.week_day_change_btn_size_down); // Кнопка уменьшения размера текста в недельном режиме
        gesture_layout = findViewById(R.id.raspisanie_day); // Слой для отслеживания жестов
        RelativeLayout raspisanie_show_layout = findViewById(R.id.raspisanie_show); // Основной слой
        new Refresh_rasp_week_or_day_starter(getApplicationContext()).start(); // Обновляем расписание
        CheckBox mCheckBox = findViewById(R.id.checkBox); // Уведомление об обновлении расписания

        new CheckGroupRaspUpdate(mCheckBox).start();
        // Кнопка увеличивающая размер текста в режиме недели
        week_day_change_btn_size_up.setOnClickListener(v -> new Week_show_resize().size_add());

        // Кнопка уменьшающая размер текста в режиме недели
        week_day_change_btn_size_down.setOnClickListener(v -> new Week_show_resize().size_dec());

        Raspisanie_show.refresh_btn.startAnimation(MainActivity.animRotate);
        Raspisanie_show.refresh_btn.setBackgroundResource(R.drawable.refresh_1);
        // Первичный вывод расписания
        new Swipe_rasp("Bottom", getApplicationContext());

        // Функция перехода на сайт с расписанием при нажатии на кнопку
        ImageView rasp_site = findViewById(R.id.rasp_site);
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
        week_day_bt1.setOnClickListener(v -> new Swipe_rasp("Left", getApplicationContext()));

        // Переход к следующему дню
        week_day_bt2.setOnClickListener(v -> new Swipe_rasp("Right", getApplicationContext()));

        // Обновить расписание
        refresh_btn.setOnClickListener(v -> new Swipe_rasp("Bottom", getApplicationContext()));
        refresh_btn_ficha.setOnClickListener(v -> {
            int random_int = new Random().nextInt(4);
            switch (random_int){
                case 0:
                    raspisanie_show_layout.startAnimation(MainActivity.animRotate_ok);
                    Ficha_achievements.put(getApplicationContext(), "ficha_refresh");
                    break;
                case 1: raspisanie_show_layout.startAnimation(MainActivity.animScale); break;
                case 2: raspisanie_show_layout.startAnimation(MainActivity.animUehalVl); break;
                case 3: raspisanie_show_layout.startAnimation(MainActivity.animUehalVp); break;
            }
        });

        // Смена недельного режима и дневного
        week_day_change_btn.setOnClickListener(v -> new Week_day_change(getApplicationContext()));

        // Отслеживание нажатий на список пар
        new ListViewDayPara_Listener(this, day_para_view);

        // Отслеживание жестов под дневным расписанием
        gesture_layout.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeRight() { new Swipe_rasp("Left", getApplicationContext()); }
            public void onSwipeLeft() { new Swipe_rasp("Right", getApplicationContext()); }
            public void onSwipeBottom(){ new Swipe_rasp("Bottom", getApplicationContext()); }
        });
    }
 }
