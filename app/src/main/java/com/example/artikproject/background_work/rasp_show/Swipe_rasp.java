package com.example.artikproject.background_work.rasp_show;

import android.content.Context;
import android.view.View;

import com.example.artikproject.R;
import com.example.artikproject.background_work.CheckInternetConnection;
import com.example.artikproject.layout.MainActivity;
import com.example.artikproject.layout.Raspisanie_show;

public class Swipe_rasp {
    public Swipe_rasp(String direction, Context context) {
        Raspisanie_show.week_day_bt1.setClickable(false);
        Raspisanie_show.week_day_bt2.setClickable(false);
        if (!Raspisanie_show.week_day_on_off) {
            switch (direction) {
                case "Left":
                    Raspisanie_show.week_day_bt1.setAnimation(MainActivity.animUehalVl);
                    MainActivity.week_day -= 1;
                    if (MainActivity.week_day == -1) { // Если будет воскресенье, то будет показана суббота
                        MainActivity.week_day = 5;
                        MainActivity.week_id -= 1;
                    }
                    break;
                case "Right":
                    Raspisanie_show.week_day_bt2.setAnimation(MainActivity.animUehalVp);
                    MainActivity.week_day += 1;
                    if (MainActivity.week_day == 6) { // Если будет воскресенье, то будет показана суббота
                        MainActivity.week_day = 0;
                        MainActivity.week_id += 1;
                    }
                    break;
                case "Bottom":
                    if (!Raspisanie_show.refresh_on_off) {
                        Raspisanie_show.refresh_btn.setClickable(false);
                        Raspisanie_show.refresh_btn_ficha.setVisibility(View.VISIBLE);
                        Raspisanie_show.refresh_on_off = true;
                        Raspisanie_show.refresh_btn.startAnimation(MainActivity.animRotate);
                        Raspisanie_show.refresh_btn.setBackgroundResource(R.drawable.refresh_1);
                        new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, context).start();
                        new Refresh_rasp_week_or_day_starter(context).start();
                    }
                    break;
            }
            if (CheckInternetConnection.getState(context)) {
                new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, context).start();
            }
            new Day_show(context);
            Raspisanie_show.week_day_bt1.setClickable(true);
            Raspisanie_show.week_day_bt2.setClickable(true);
        } else {
            Raspisanie_show.refresh_on_off = true;
            Raspisanie_show.refresh_btn.setClickable(false);
            Raspisanie_show.refresh_btn_ficha.setVisibility(View.VISIBLE);
            Raspisanie_show.refresh_btn.startAnimation(MainActivity.animRotate);
            Raspisanie_show.refresh_btn.setBackgroundResource(R.drawable.refresh_1);
            switch (direction) {
                case "Left": MainActivity.week_id -= 1; break;
                case "Right": MainActivity.week_id += 1; break;
            }
            if (CheckInternetConnection.getState(context)) {
                new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, context).start();
            }
            new Refresh_rasp_week_or_day_starter(context).start();
        }
    }
}
