package ru.agpu.artikproject.domain.repository

import ru.agpu.artikproject.domain.models.WeeksListItem

interface WeeksListRepository {
    fun get(): List<WeeksListItem>
    fun getByLikeStartDate(startDate: String): List<WeeksListItem>
    fun getByWeekId(weekId: Int): List<WeeksListItem>

    fun set(weeksListItems: List<WeeksListItem>)
}