package ru.oganesyanartem.core.data.repository

import ru.oganesyanartem.core.domain.repository.CurrentWeekDayRepository
import java.util.*

class CurrentWeekDayImpl: CurrentWeekDayRepository {
    override fun get(): Int {
        val cal = Calendar.getInstance()
        cal.time = Date()
        var weekDay = cal[Calendar.DAY_OF_WEEK] - 2
        // Если будет воскресенье, то будет показана суббота
        if (weekDay == -1) weekDay = 5
        return weekDay
    }
}