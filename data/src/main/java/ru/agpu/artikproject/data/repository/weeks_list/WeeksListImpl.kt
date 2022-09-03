package ru.agpu.artikproject.data.repository.weeks_list

import android.content.Context
import ru.agpu.artikproject.domain.models.WeeksListItem
import ru.agpu.artikproject.domain.repository.FullWeekListRepository

class WeeksListImpl(private val context: Context) : FullWeekListRepository {
    override fun get(): List<WeeksListItem> {
        var weeksList = WeeksListGetFromLocal().get(context = context)
        if (weeksList.isEmpty()) {
            weeksList = FullWeekListGetFromApi().get()
            if (weeksList.isNotEmpty())
                set(weeksList)
        }
        return weeksList
    }

    override fun getByLikeStartDate(startDate: String): List<WeeksListItem> {
        return get().filter { it.startDate.contains(startDate, ignoreCase = true) }
    }

    override fun getByWeekId(weekId: Int): List<WeeksListItem> {
        return get().filter { it.weekId == weekId }
    }

    override fun set(weeksListItems: List<WeeksListItem>) {
        WeeksListSetToLocal().set(context = context, weeksList = weeksListItems)
    }
}