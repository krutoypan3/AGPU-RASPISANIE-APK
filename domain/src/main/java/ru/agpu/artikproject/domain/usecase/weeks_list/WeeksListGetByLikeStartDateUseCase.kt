package ru.agpu.artikproject.domain.usecase.weeks_list

import ru.agpu.artikproject.domain.models.WeeksListItem
import ru.agpu.artikproject.domain.repository.WeeksListRepository

class WeeksListGetByLikeStartDateUseCase(
    private val weeksListRepository: WeeksListRepository,
    private val startDate: String
) {
    fun execute(): List<WeeksListItem> {
        return weeksListRepository.getByLikeStartDate(startDate = startDate)
    }
}