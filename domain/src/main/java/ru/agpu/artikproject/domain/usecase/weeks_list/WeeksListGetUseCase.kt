package ru.agpu.artikproject.domain.usecase.weeks_list

import ru.agpu.artikproject.domain.models.WeeksListItem
import ru.agpu.artikproject.domain.repository.FullWeekListRepository

class WeeksListGetUseCase(private val fullWeekListRepository: FullWeekListRepository) {
    fun execute(): List<WeeksListItem> {
        return fullWeekListRepository.get()
    }
}