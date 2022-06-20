package ru.agpu.artikproject.background_work.rasp_show;

import android.app.Activity;
import android.view.View;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.layout.MainActivity;
import ru.agpu.artikproject.layout.Raspisanie_show;

public class Week_day_change {
    /**
     * Изменяет свойства видимости объектов при переключении режимов просмотра расписания
     * (недельный \ дневной)
     * @param activity Контекст приложения
     */
    public Week_day_change(Activity activity){
        if (!Raspisanie_show.week_day_on_off){
            Raspisanie_show.week_day_change_btn.setImageResource(R.drawable.ic_baseline_today_24);
            Raspisanie_show.week_day_change_btn.setAnimation(MainActivity.animScale);
            Raspisanie_show.week_day_on_off = true;
            new Week_show(activity);
            activity.findViewById(R.id.day_para_view_rec).setVisibility(View.INVISIBLE);
            Raspisanie_show.week_para_view.setVisibility(View.VISIBLE);
            Raspisanie_show.gesture_layout.setVisibility(View.INVISIBLE);
            Raspisanie_show.week_day_change_btn_size_up.setVisibility(View.VISIBLE);
            Raspisanie_show.week_day_change_btn_size_down.setVisibility(View.VISIBLE);
        }
        else{
            Raspisanie_show.week_day_change_btn.setImageResource(R.drawable.ic_baseline_date_range_24);
            Raspisanie_show.week_day_change_btn.setAnimation(MainActivity.animScale);
            activity.findViewById(R.id.day_para_view_rec).setVisibility(View.VISIBLE);
            Raspisanie_show.week_para_view.setVisibility(View.INVISIBLE);
            Raspisanie_show.week_day_on_off = false;
            new Day_show(activity);
            Raspisanie_show.gesture_layout.setVisibility(View.VISIBLE);
            Raspisanie_show.week_day_change_btn_size_up.setVisibility(View.INVISIBLE);
            Raspisanie_show.week_day_change_btn_size_down.setVisibility(View.INVISIBLE);
        }
    }
}
