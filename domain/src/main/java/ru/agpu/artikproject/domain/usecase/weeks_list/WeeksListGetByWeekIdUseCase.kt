package ru.agpu.artikproject.domain.usecase.weeks_list

import ru.agpu.artikproject.domain.models.WeeksListItem
import ru.agpu.artikproject.domain.repository.FullWeekListRepository

class WeeksListGetByWeekIdUseCase(private val fullWeekListRepository: FullWeekListRepository, private val weekId: Int) {
    fun execute(): List<WeeksListItem> {
        return fullWeekListRepository.getByWeekId(weekId = weekId)
    }
}