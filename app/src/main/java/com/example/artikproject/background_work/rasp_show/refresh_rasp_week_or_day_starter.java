package com.example.artikproject.background_work.rasp_show;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.artikproject.R;
import com.example.artikproject.layout.MainActivity;
import com.example.artikproject.layout.raspisanie_show;

import java.util.concurrent.TimeUnit;

public class refresh_rasp_week_or_day_starter extends AsyncTask<Void, Void, Void> {
    Context context;

    public refresh_rasp_week_or_day_starter(Context context){
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Void doInBackground(Void... voids) {
        while (raspisanie_show.refresh_on_off){
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Ошибка в классе refresh_day_show (doInBackground)");
            }
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() { // Это нужно для вызова вне основного потока
            @Override
            public void run() {
            if (!raspisanie_show.week_day_on_off) { new day_show(context); }
            else{ new week_show(context); }
            if (raspisanie_show.refresh_successful) { raspisanie_show.refresh_btn.setBackgroundResource(R.drawable.refresh_2); }
            else{ raspisanie_show.refresh_btn.setBackgroundResource(R.drawable.refresh_0); }
            raspisanie_show.refresh_btn.setClickable(true);
            raspisanie_show.refresh_btn_ficha.setVisibility(View.INVISIBLE);
            raspisanie_show.refresh_btn.startAnimation(MainActivity.animRotate_ok);
            raspisanie_show.week_day_bt1.setClickable(true);
            raspisanie_show.week_day_bt2.setClickable(true);
            raspisanie_show.refresh_btn_ficha.setVisibility(View.INVISIBLE);
            }
        });
        return null;
    }
}
