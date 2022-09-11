package ru.oganesyanartem.core.domain.usecase.weeks_list

import ru.oganesyanartem.core.domain.models.WeeksListItem
import ru.oganesyanartem.core.domain.repository.WeeksListRepository

class WeeksListGetByWeekIdUseCase(private val weeksListRepository: WeeksListRepository, private val weekId: Int) {
    fun execute(): List<WeeksListItem> {
        return weeksListRepository.getByWeekId(weekId = weekId)
    }
}