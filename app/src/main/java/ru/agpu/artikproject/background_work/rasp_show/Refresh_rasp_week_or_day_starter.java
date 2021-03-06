package ru.agpu.artikproject.background_work.rasp_show;


import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.concurrent.TimeUnit;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.layout.MainActivity;
import ru.agpu.artikproject.layout.Raspisanie_show;

public class Refresh_rasp_week_or_day_starter extends Thread {
    final Activity activity;

    /**
     * Класс отвечающий за запуск обновления расписания в недельном или днемном режиме
     * @param activity Контекст приложения
     */
    public Refresh_rasp_week_or_day_starter(Activity activity){
        this.activity = activity;
    }

    @Override
    public void run() {
        while (Raspisanie_show.refresh_on_off){
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Это нужно для вызова вне основного потока
        new Handler(Looper.getMainLooper()).post(() -> {
            if (!Raspisanie_show.week_day_on_off) { new Day_show(activity); }
            else{ new Week_show(activity.getApplicationContext()); }
            if (Raspisanie_show.refresh_successful) { Raspisanie_show.refresh_btn.setBackgroundResource(R.drawable.refresh_2); }
            else{ Raspisanie_show.refresh_btn.setBackgroundResource(R.drawable.refresh_0); }
            Raspisanie_show.refresh_btn.setClickable(true);
            Raspisanie_show.refresh_btn_ficha.setVisibility(View.INVISIBLE);
            Raspisanie_show.refresh_btn.startAnimation(MainActivity.animRotate_ok);
            Raspisanie_show.week_day_bt1.setClickable(true);
            Raspisanie_show.week_day_bt2.setClickable(true);
            Raspisanie_show.refresh_btn_ficha.setVisibility(View.INVISIBLE);
        });
    }
}
