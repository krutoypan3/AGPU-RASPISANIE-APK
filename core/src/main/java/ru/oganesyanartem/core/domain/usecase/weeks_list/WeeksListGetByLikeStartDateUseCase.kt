package ru.oganesyanartem.core.domain.usecase.weeks_list

import ru.oganesyanartem.core.domain.models.WeeksListItem
import ru.oganesyanartem.core.domain.repository.WeeksListRepository

class WeeksListGetByLikeStartDateUseCase(
    private val weeksListRepository: WeeksListRepository,
    private val startDate: String
) {
    fun execute(): List<WeeksListItem> {
        return weeksListRepository.getByLikeStartDate(startDate = startDate)
    }
}