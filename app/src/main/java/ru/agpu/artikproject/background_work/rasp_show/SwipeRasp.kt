package ru.agpu.artikproject.background_work.rasp_show

import android.view.View
import android.widget.Button
import android.widget.ImageView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CheckInternetConnection
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow.Companion.refresh_on_off
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow.Companion.week_day_on_off
import ru.agpu.artikproject.background_work.site_parse.GetRasp
import ru.agpu.artikproject.presentation.layout.MainActivity

class SwipeRasp(direction: String, view: View) {
    init {
        val weekDayBt1 = view.findViewById<Button>(R.id.week_day_bt1)
        val weekDayBt2 = view.findViewById<Button>(R.id.week_day_bt2)
        val refreshBtn = view.findViewById<ImageView>(R.id.refresh_btn)
        val refreshBtnFicha = view.findViewById<ImageView>(R.id.refresh_btn_all)

        weekDayBt1.isClickable = false
        weekDayBt2.isClickable = false
        if (!week_day_on_off) {
            when (direction) {
                "Left" -> {
                    weekDayBt1.animation = MainActivity.animUehalVl
                    MainActivity.week_day -= 1
                    if (MainActivity.week_day == -1) { // Если будет воскресенье, то будет показана суббота
                        MainActivity.week_day = 5
                        MainActivity.week_id -= 1
                    }
                }

                "Right" -> {
                    weekDayBt2.animation = MainActivity.animUehalVp
                    MainActivity.week_day += 1
                    if (MainActivity.week_day == 6) { // Если будет воскресенье, то будет показана суббота
                        MainActivity.week_day = 0
                        MainActivity.week_id += 1
                    }
                }

                "Bottom" -> if (!refresh_on_off) {
                    refreshBtn.isClickable = false
                    refreshBtnFicha.visibility = View.VISIBLE
                    refreshBtn.startAnimation(MainActivity.animRotate)
                    refreshBtn.setBackgroundResource(R.drawable.refresh_1)
                    GetRasp(
                        MainActivity.selectedItem_id,
                        MainActivity.selectedItem_type,
                        MainActivity.selectedItem,
                        MainActivity.week_id,
                        view.context,
                        null
                    ).start()
                    RefreshRaspWeekOrDayStarter(view).start()
                }
            }
            DayShow(view)
            weekDayBt1.isClickable = true
            weekDayBt2.isClickable = true
        } else {
            refreshBtn.isClickable = false
            refreshBtnFicha.visibility = View.VISIBLE
            refreshBtn.startAnimation(MainActivity.animRotate)
            refreshBtn.setBackgroundResource(R.drawable.refresh_1)
            when (direction) {
                "Left" -> MainActivity.week_id -= 1
                "Right" -> MainActivity.week_id += 1
            }
            if (CheckInternetConnection.getState(view.context)) {
                GetRasp(
                    MainActivity.selectedItem_id,
                    MainActivity.selectedItem_type,
                    MainActivity.selectedItem,
                    MainActivity.week_id,
                    view.context,
                    null
                ).start()
            }
            RefreshRaspWeekOrDayStarter(view).start()
        }
    }
}