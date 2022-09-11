package ru.oganesyanartem.core.domain.repository

import ru.oganesyanartem.core.domain.models.WeeksListItem

interface WeeksListRepository {
    fun get(): List<WeeksListItem>
    fun getByLikeStartDate(startDate: String): List<WeeksListItem>
    fun getByWeekId(weekId: Int): List<WeeksListItem>

    fun set(weeksListItems: List<WeeksListItem>)
}