package ru.agpu.artikproject.background_work.rasp_show;


import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow;
import ru.agpu.artikproject.presentation.layout.MainActivity;

public class Refresh_rasp_week_or_day_starter extends Thread {
    final View view;

    /**
     * Класс отвечающий за запуск обновления расписания в недельном или днемном режиме
     * @param view View фрагмента приложения
     */
    public Refresh_rasp_week_or_day_starter(View view){
        this.view = view;
    }

    @Override
    public void run() {
        while (FragmentScheduleShow.refresh_on_off){
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ImageView refresh_btn = view.findViewById(R.id.refresh_btn);
        ImageView refresh_btn_ficha = view.findViewById(R.id.refresh_btn_all);
        Button week_day_bt1 = view.findViewById(R.id.week_day_bt1);
        Button week_day_bt2 = view.findViewById(R.id.week_day_bt2);

        // Это нужно для вызова вне основного потока
        new Handler(Looper.getMainLooper()).post(() -> {
            try {
                if (!FragmentScheduleShow.week_day_on_off)
                    new Day_show(view);
                else
                    new Week_show(view.getContext());

                if (view.isShown()) {
                    if (FragmentScheduleShow.refresh_successful)
                        refresh_btn.setBackgroundResource(R.drawable.refresh_2);
                    else
                        refresh_btn.setBackgroundResource(R.drawable.refresh_0);
                    refresh_btn.setClickable(true);
                    refresh_btn_ficha.setVisibility(View.INVISIBLE);
                    refresh_btn.startAnimation(MainActivity.animRotate_ok);
                    week_day_bt1.setClickable(true);
                    week_day_bt2.setClickable(true);
                    refresh_btn_ficha.setVisibility(View.INVISIBLE);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}
