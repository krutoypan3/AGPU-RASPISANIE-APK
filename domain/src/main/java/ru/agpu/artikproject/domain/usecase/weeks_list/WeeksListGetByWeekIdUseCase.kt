package ru.agpu.artikproject.domain.usecase.weeks_list

import ru.agpu.artikproject.domain.models.WeeksListItem
import ru.agpu.artikproject.domain.repository.WeeksListRepository

class WeeksListGetByWeekIdUseCase(private val weeksListRepository: WeeksListRepository, private val weekId: Int) {
    fun execute(): List<WeeksListItem> {
        return weeksListRepository.getByWeekId(weekId = weekId)
    }
}