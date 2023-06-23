package ru.agpu.artikproject.background_work.rasp_show;

import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow;
import ru.agpu.artikproject.presentation.layout.MainActivity;

public class Week_day_change {
    /**
     * Изменяет свойства видимости объектов при переключении режимов просмотра расписания
     * (недельный \ дневной)
     * @param view Контекст приложения
     */
    public Week_day_change(View view){
        ImageView week_day_change_btn = view.findViewById(R.id.week_day_change_btn);
        TableLayout week_para_view = view.findViewById(R.id.week_para_view);
        ConstraintLayout gesture_layout = view.findViewById(R.id.raspisanie_day);
        ImageView week_day_change_btn_size_up = view.findViewById(R.id.week_day_change_btn_size_up);
        ImageView week_day_change_btn_size_down = view.findViewById(R.id.week_day_change_btn_size_down);

        if (!FragmentScheduleShow.Companion.getWeek_day_on_off()){
            week_day_change_btn.setImageResource(R.drawable.ic_baseline_today_24);
            week_day_change_btn.setAnimation(MainActivity.animScale);
            FragmentScheduleShow.Companion.setWeek_day_on_off(true);
            new Week_show(view.getContext());
            view.findViewById(R.id.day_para_view_rec).setVisibility(View.INVISIBLE);
            week_para_view.setVisibility(View.VISIBLE);
            gesture_layout.setVisibility(View.INVISIBLE);
            week_day_change_btn_size_up.setVisibility(View.VISIBLE);
            week_day_change_btn_size_down.setVisibility(View.VISIBLE);
        }
        else{
            week_day_change_btn.setImageResource(R.drawable.ic_baseline_date_range_24);
            week_day_change_btn.setAnimation(MainActivity.animScale);
            view.findViewById(R.id.day_para_view_rec).setVisibility(View.VISIBLE);
            week_para_view.setVisibility(View.INVISIBLE);
            FragmentScheduleShow.Companion.setWeek_day_on_off(false);
            new DayShow(view);
            gesture_layout.setVisibility(View.VISIBLE);
            week_day_change_btn_size_up.setVisibility(View.INVISIBLE);
            week_day_change_btn_size_down.setVisibility(View.INVISIBLE);
        }
    }
}
