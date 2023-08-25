package ru.agpu.artikproject.background_work.rasp_show

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animRotate_ok
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow.Companion.refresh_on_off
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow.Companion.refresh_successful
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow.Companion.week_day_on_off
import ru.agpu.artikproject.presentation.layout.MainActivity

/**
 * Класс отвечающий за запуск обновления расписания в недельном или днемном режиме
 * @param view View фрагмента приложения
 */
class RefreshRaspWeekOrDayStarter(val view: View): Thread() {
    override fun run() {
        while (refresh_on_off) {
            sleep(500)
        }
        val refreshBtn = view.findViewById<ImageView>(R.id.refresh_btn)
        val refreshBtnFicha = view.findViewById<ImageView>(R.id.refresh_btn_all)
        val weekDayBt1 = view.findViewById<Button>(R.id.week_day_bt1)
        val weekDayBt2 = view.findViewById<Button>(R.id.week_day_bt2)

        // Это нужно для вызова вне основного потока
        Handler(Looper.getMainLooper()).post {
            try {
                if (!week_day_on_off) DayShow(view)
                else WeekShow(view.context)

                if (view.isShown) {
                    if (refresh_successful) refreshBtn.setBackgroundResource(R.drawable.refresh_2)
                    else refreshBtn.setBackgroundResource(R.drawable.refresh_0)
                    refreshBtn.isClickable = true
                    refreshBtnFicha.visibility = View.INVISIBLE
                    refreshBtn.startAnimation(animRotate_ok)
                    weekDayBt1.isClickable = true
                    weekDayBt2.isClickable = true
                    refreshBtnFicha.visibility = View.INVISIBLE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}