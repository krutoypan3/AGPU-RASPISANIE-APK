package ru.agpu.artikproject.background_work.main_show

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.TextView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.AppData.AppDate.weekId
import ru.oganesyanartem.core.data.repository.weeks_list.WeeksListImpl
import ru.oganesyanartem.core.domain.repository.WeeksListRepository
import ru.oganesyanartem.core.domain.usecase.weeks_list.WeeksListGetByWeekIdUseCase
import java.text.SimpleDateFormat

/**
 * Устанавливает дату на главном экране
 * @param act Активити
 */
@SuppressLint("SimpleDateFormat")
class UpdateDateInMainActivity(val act: Activity): Thread() {
    override fun run() {
        try {
            val weeksListRepository: WeeksListRepository = WeeksListImpl(act.applicationContext)
            val weekList = WeeksListGetByWeekIdUseCase(weeksListRepository, weekId).execute()
            if (weekList.isNotEmpty()) {
                val sPo = weekList[0].startDate + " " + weekList[0].endDate
                act.runOnUiThread {
                    try {
                        val currentWeek: TextView = act.findViewById(R.id.subtitle)
                        currentWeek.text = sPo
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                val day = SimpleDateFormat("dd.MM.yyyy, EEEE").format(ChangeDay.chosenDateCalendar.time)
                val todayDate: String = act.getString(R.string.Curent_day) + " " + day
                act.runOnUiThread {
                    try {
                        val today: TextView = act.findViewById(R.id.main_activity_text)
                        today.text = todayDate
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
                act.runOnUiThread {
                    try {
                        val currentWeek: TextView = act.findViewById(R.id.subtitle)
                        currentWeek.text = act.getString(R.string.weeks_not_found)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}