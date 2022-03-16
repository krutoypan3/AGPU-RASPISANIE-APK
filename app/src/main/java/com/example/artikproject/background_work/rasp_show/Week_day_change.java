package com.example.artikproject.background_work.rasp_show;

import android.content.Context;
import android.view.View;

import com.example.artikproject.R;
import com.example.artikproject.layout.MainActivity;
import com.example.artikproject.layout.Raspisanie_show;

public class Week_day_change {
    /**
     * Изменяет свойства видимости объектов при переключении режимов просмотра расписания
     * (недельный \ дневной)
     * @param context Контекст приложения
     */
    public Week_day_change(Context context){
        if (!Raspisanie_show.week_day_on_off){
            Raspisanie_show.week_day_change_btn.setImageResource(R.drawable.ic_baseline_today_24);
            Raspisanie_show.week_day_change_btn.setAnimation(MainActivity.animScale);
            new Week_show(context);
            Raspisanie_show.para_view.setVisibility(View.INVISIBLE);
            Raspisanie_show.tableLayout.setVisibility(View.VISIBLE);
            Raspisanie_show.week_day_on_off = true;
            Raspisanie_show.gesture_layout.setVisibility(View.INVISIBLE);
            Raspisanie_show.week_day_change_btn_size_up.setVisibility(View.VISIBLE);
            Raspisanie_show.week_day_change_btn_size_down.setVisibility(View.VISIBLE);
        }
        else{
            Raspisanie_show.week_day_change_btn.setImageResource(R.drawable.ic_baseline_date_range_24);
            Raspisanie_show.week_day_change_btn.setAnimation(MainActivity.animScale);
            Raspisanie_show.para_view.setVisibility(View.VISIBLE);
            Raspisanie_show.tableLayout.setVisibility(View.INVISIBLE);
            Raspisanie_show.week_day_on_off = false;
            new Day_show(context);
            Raspisanie_show.gesture_layout.setVisibility(View.VISIBLE);
            Raspisanie_show.week_day_change_btn_size_up.setVisibility(View.INVISIBLE);
            Raspisanie_show.week_day_change_btn_size_down.setVisibility(View.INVISIBLE);
        }
    }
}
