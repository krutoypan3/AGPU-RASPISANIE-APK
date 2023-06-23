package ru.agpu.artikproject.background_work.rasp_show;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.CheckInternetConnection;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow;
import ru.agpu.artikproject.background_work.site_parse.GetRasp;
import ru.agpu.artikproject.presentation.layout.MainActivity;

public class Swipe_rasp {
    public Swipe_rasp(String direction, View view) {
        Button week_day_bt1 = view.findViewById(R.id.week_day_bt1);
        Button week_day_bt2 = view.findViewById(R.id.week_day_bt2);
        ImageView refresh_btn = view.findViewById(R.id.refresh_btn);
        ImageView refresh_btn_ficha = view.findViewById(R.id.refresh_btn_all);

        week_day_bt1.setClickable(false);
        week_day_bt2.setClickable(false);
        if (!FragmentScheduleShow.Companion.getWeek_day_on_off()) {
            switch (direction) {
                case "Left":
                    week_day_bt1.setAnimation(MainActivity.animUehalVl);
                    MainActivity.week_day -= 1;
                    if (MainActivity.week_day == -1) { // Если будет воскресенье, то будет показана суббота
                        MainActivity.week_day = 5;
                        MainActivity.week_id -= 1;
                    }
                    break;
                case "Right":
                    week_day_bt2.setAnimation(MainActivity.animUehalVp);
                    MainActivity.week_day += 1;
                    if (MainActivity.week_day == 6) { // Если будет воскресенье, то будет показана суббота
                        MainActivity.week_day = 0;
                        MainActivity.week_id += 1;
                    }
                    break;
                case "Bottom":
                    if (!FragmentScheduleShow.Companion.getRefresh_on_off()) {
                        refresh_btn.setClickable(false);
                        refresh_btn_ficha.setVisibility(View.VISIBLE);
                        refresh_btn.startAnimation(MainActivity.animRotate);
                        refresh_btn.setBackgroundResource(R.drawable.refresh_1);
                        new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, view.getContext(), null).start();
                        new Refresh_rasp_week_or_day_starter(view).start();
                    }
                    break;
            }
            new DayShow(view);
            week_day_bt1.setClickable(true);
            week_day_bt2.setClickable(true);
        } else {
            refresh_btn.setClickable(false);
            refresh_btn_ficha.setVisibility(View.VISIBLE);
            refresh_btn.startAnimation(MainActivity.animRotate);
            refresh_btn.setBackgroundResource(R.drawable.refresh_1);
            switch (direction) {
                case "Left": MainActivity.week_id -= 1; break;
                case "Right": MainActivity.week_id += 1; break;
            }
            if (CheckInternetConnection.getState(view.getContext())) {
                new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, view.getContext(), null).start();
            }
            new Refresh_rasp_week_or_day_starter(view).start();
        }
    }
}
