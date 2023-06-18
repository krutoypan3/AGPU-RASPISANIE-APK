package ru.agpu.artikproject.background_work.main_show.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.Random;

import kotlin.jvm.Synchronized;
import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.OnSwipeTouchListener;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;
import ru.agpu.artikproject.background_work.rasp_show.RaspUpdateCheckBoxListener;
import ru.agpu.artikproject.background_work.rasp_show.Refresh_rasp_week_or_day_starter;
import ru.agpu.artikproject.background_work.rasp_show.Swipe_rasp;
import ru.agpu.artikproject.background_work.rasp_show.Week_day_change;
import ru.agpu.artikproject.background_work.rasp_show.Week_show_resize;
import ru.agpu.artikproject.background_work.settings_layout.ficha.Ficha_achievements;
import ru.agpu.artikproject.presentation.layout.MainActivity;

public class FragmentScheduleShow extends Fragment {
    // TODO нужно сделать переменную synchronize
    public static boolean refresh_on_off = false;
    public static boolean week_day_on_off = false;
    public static boolean refresh_successful = true;

    public FragmentScheduleShow() {
        super(R.layout.fragment_main_activity_schedule_show);
    }

    @Override
    public void onResume(){
        super.onResume();
        new RaspUpdateCheckBoxListener(getView());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Сохраняем последнее открытое расписание
        MySharedPreferences.put(view.getContext(), "selectedItem", MainActivity.selectedItem);
        MySharedPreferences.put(view.getContext(), "selectedItem_type", MainActivity.selectedItem_type);
        MySharedPreferences.put(view.getContext(), "selectedItem_id", MainActivity.selectedItem_id);

        Button week_day_bt1 = view.findViewById(R.id.week_day_bt1); // Кнопка перехода к предыдущему дню
        Button week_day_bt2 = view.findViewById(R.id.week_day_bt2); // Кнопка перехода к следующему дню
        ImageView week_day_change_btn = view.findViewById(R.id.week_day_change_btn); // Кнопка смены режима просмотра расписания [День / Неделя]
        ImageView refresh_btn_ficha = view.findViewById(R.id.refresh_btn_all); // Кнопка перекручивания расп (фича)
        ImageView refresh_btn = view.findViewById(R.id.refresh_btn); // Кнопка обновления расписания
        ImageView week_day_change_btn_size_up = view.findViewById(R.id.week_day_change_btn_size_up); // Кнопка увеличения размера текста в недельном режиме
        ImageView week_day_change_btn_size_down = view.findViewById(R.id.week_day_change_btn_size_down); // Кнопка уменьшения размера текста в недельном режиме
        ConstraintLayout gesture_layout = view.findViewById(R.id.raspisanie_day); // Слой для отслеживания жестов
        RelativeLayout raspisanie_show_layout = view.findViewById(R.id.raspisanie_show); // Основной слой


        new Refresh_rasp_week_or_day_starter(view).start(); // Обновляем расписание

        // Кнопка увеличивающая размер текста в режиме недели
        week_day_change_btn_size_up.setOnClickListener(v -> new Week_show_resize().size_add());

        // Кнопка уменьшающая размер текста в режиме недели
        week_day_change_btn_size_down.setOnClickListener(v -> new Week_show_resize().size_dec());

        refresh_btn.startAnimation(MainActivity.animRotate);
        refresh_btn.setBackgroundResource(R.drawable.refresh_1);
        // Первичный вывод расписания
        new Swipe_rasp("Bottom", view);

        // Функция перехода на сайт с расписанием при нажатии на кнопку
        ImageView rasp_site = view.findViewById(R.id.rasp_site);
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
        week_day_bt1.setOnClickListener(v -> new Swipe_rasp("Left", view));

        // Переход к следующему дню
        week_day_bt2.setOnClickListener(v -> new Swipe_rasp("Right", view));

        // Обновить расписание
        refresh_btn.setOnClickListener(v -> new Swipe_rasp("Bottom", view));
        refresh_btn_ficha.setOnClickListener(v -> {
            int random_int = new Random().nextInt(4);
            switch (random_int){
                case 0:
                    raspisanie_show_layout.startAnimation(MainActivity.animRotate_ok);
                    Ficha_achievements.put(view.getContext(), "ficha_refresh");
                    break;
                case 1: raspisanie_show_layout.startAnimation(MainActivity.animScale); break;
                case 2: raspisanie_show_layout.startAnimation(MainActivity.animUehalVl); break;
                case 3: raspisanie_show_layout.startAnimation(MainActivity.animUehalVp); break;
            }
        });

        // Смена недельного режима и дневного
        week_day_change_btn.setOnClickListener(v -> new Week_day_change(view));

        view.findViewById(R.id.day_para_view_rec).setOnTouchListener(new OnSwipeTouchListener(view.getContext()) {
            public void onSwipeRight() { new Swipe_rasp("Left", view); }
            public void onSwipeLeft() { new Swipe_rasp("Right", view); }
        });


        // Отслеживание жестов под дневным расписанием
        gesture_layout.setOnTouchListener(new OnSwipeTouchListener(view.getContext()) {
            public void onSwipeRight() { new Swipe_rasp("Left", view); }
            public void onSwipeLeft() { new Swipe_rasp("Right", view); }
            public void onSwipeBottom(){ new Swipe_rasp("Bottom", view); }
        });
    }
}
