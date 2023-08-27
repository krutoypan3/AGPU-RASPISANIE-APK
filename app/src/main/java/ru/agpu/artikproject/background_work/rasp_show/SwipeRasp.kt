package ru.agpu.artikproject.background_work.rasp_show

import android.view.View
import android.widget.Button
import android.widget.ImageView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CheckInternetConnection
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animRotate
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animUehalVl
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animUehalVp
import ru.agpu.artikproject.background_work.datebase.AppData.AppDate.weekDay
import ru.agpu.artikproject.background_work.datebase.AppData.AppDate.weekId
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItem
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItemId
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItemType
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
                    weekDayBt1.animation = animUehalVl
                    weekDay -= 1
                    if (weekDay == -1) { // Если будет воскресенье, то будет показана суббота
                        weekDay = 5
                        weekId -= 1
                    }
                }

                "Right" -> {
                    weekDayBt2.animation = animUehalVp
                    weekDay += 1
                    if (weekDay == 6) { // Если будет воскресенье, то будет показана суббота
                        weekDay = 0
                        weekId += 1
                    }
                }

                "Bottom" -> if (!refresh_on_off) {
                    refreshBtn.isClickable = false
                    refreshBtnFicha.visibility = View.VISIBLE
                    refreshBtn.startAnimation(animRotate)
                    refreshBtn.setBackgroundResource(R.drawable.refresh_1)
                    GetRasp(
                        selectedItemId ?: "",
                        selectedItemType ?: "",
                        selectedItem ?: "",
                        weekId,
                        view.context
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
            refreshBtn.startAnimation(animRotate)
            refreshBtn.setBackgroundResource(R.drawable.refresh_1)
            when (direction) {
                "Left" -> weekId -= 1
                "Right" -> weekId += 1
            }
            if (CheckInternetConnection.getState(view.context)) {
                GetRasp(
                    selectedItemId ?: "",
                    selectedItemType ?: "",
                    selectedItem ?: "",
                    weekId,
                    view.context
                ).start()
            }
            RefreshRaspWeekOrDayStarter(view).start()
        }
    }
}