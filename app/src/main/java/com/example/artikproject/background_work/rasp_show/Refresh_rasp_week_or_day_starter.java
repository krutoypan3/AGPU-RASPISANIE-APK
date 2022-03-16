package com.example.artikproject.background_work.rasp_show;


import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.artikproject.R;
import com.example.artikproject.layout.MainActivity;
import com.example.artikproject.layout.Raspisanie_show;

import java.util.concurrent.TimeUnit;

public class Refresh_rasp_week_or_day_starter extends Thread {
    Context context;

    /**
     * Класс отвечающий за запуск обновления расписания в недельном или днемном режиме
     * @param context Контекст приложения
     */
    public Refresh_rasp_week_or_day_starter(Context context){
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
            if (!Raspisanie_show.week_day_on_off) { new Day_show(context); }
            else{ new Week_show(context); }
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
