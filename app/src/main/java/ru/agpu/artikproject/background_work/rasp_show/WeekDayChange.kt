package ru.agpu.artikproject.background_work.rasp_show

import android.view.View
import android.widget.ImageView
import android.widget.TableLayout
import androidx.constraintlayout.widget.ConstraintLayout
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animScale
import ru.agpu.artikproject.presentation.layout.fragment.ScheduleShowFragment.Companion.week_day_on_off

/**
 * Изменяет свойства видимости объектов при переключении режимов просмотра расписания
 * (недельный \ дневной)
 * @param view Контекст приложения
 */
class WeekDayChange(view: View) {
    init {
        val weekDayChangeBtn = view.findViewById<ImageView>(R.id.week_day_change_btn)
        val weekParaView = view.findViewById<TableLayout>(R.id.week_para_view)
        val gestureLayout = view.findViewById<ConstraintLayout>(R.id.raspisanie_day)
        val weekDayChangeBtnSizeUp = view.findViewById<ImageView>(R.id.week_day_change_btn_size_up)
        val weekDayChangeBtnSizeDown = view.findViewById<ImageView>(R.id.week_day_change_btn_size_down)

        if (!week_day_on_off) {
            weekDayChangeBtn.setImageResource(R.drawable.ic_baseline_today_24)
            weekDayChangeBtn.animation = animScale
            week_day_on_off = true
            WeekShow(view.context)
            view.findViewById<View>(R.id.day_para_view_rec).visibility = View.INVISIBLE
            weekParaView.visibility = View.VISIBLE
            gestureLayout.visibility = View.INVISIBLE
            weekDayChangeBtnSizeUp.visibility = View.VISIBLE
            weekDayChangeBtnSizeDown.visibility = View.VISIBLE
        } else {
            weekDayChangeBtn.setImageResource(R.drawable.ic_baseline_date_range_24)
            weekDayChangeBtn.animation = animScale
            view.findViewById<View>(R.id.day_para_view_rec).visibility = View.VISIBLE
            weekParaView.visibility = View.INVISIBLE
            week_day_on_off = false
            DayShow(view)
            gestureLayout.visibility = View.VISIBLE
            weekDayChangeBtnSizeUp.visibility = View.INVISIBLE
            weekDayChangeBtnSizeDown.visibility = View.INVISIBLE
        }
    }
}