package ru.oganesyanartem.core.data.repository.weeks_list

import android.content.Context
import ru.oganesyanartem.core.domain.models.WeeksListItem
import ru.oganesyanartem.core.domain.repository.WeeksListRepository

class WeeksListImpl(private val context: Context) : WeeksListRepository {
    override fun get(forceUpdate: Boolean?): List<WeeksListItem> {
        var weeksList = WeeksListGetFromLocal().get(context = context)
        if (weeksList.isEmpty() || (forceUpdate == true)) {
            weeksList = WeeksListGetFromApi().get()
            if (weeksList.isNotEmpty())
                set(weeksList)
        }
        return weeksList
    }

    override fun getByLikeStartDate(startDate: String): List<WeeksListItem> {
        var weeks = get().filter { it.startDate.contains(startDate, ignoreCase = true) }
        if (weeks.isEmpty()) {
            weeks = get(forceUpdate = true).filter { it.startDate.contains(startDate, ignoreCase = true) }
        }
        return weeks
    }

    override fun getByWeekId(weekId: Int): List<WeeksListItem> {
        return get().filter { it.weekId == weekId }
    }

    override fun set(weeksListItems: List<WeeksListItem>) {
        WeeksListSetToLocal().set(context = context, weeksList = weeksListItems)
    }
}