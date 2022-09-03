package ru.agpu.artikproject.domain.usecase.weeks_list

import ru.agpu.artikproject.domain.models.WeeksListItem
import ru.agpu.artikproject.domain.repository.FullWeekListRepository

class WeeksListGetByLikeStartDateUseCase(
    private val fullWeekListRepository: FullWeekListRepository,
    private val startDate: String
) {
    fun execute(): List<WeeksListItem> {
        return fullWeekListRepository.getByLikeStartDate(startDate = startDate)
    }
}