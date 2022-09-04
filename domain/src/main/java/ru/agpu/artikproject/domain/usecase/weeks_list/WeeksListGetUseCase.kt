package ru.agpu.artikproject.domain.usecase.weeks_list

import ru.agpu.artikproject.domain.models.WeeksListItem
import ru.agpu.artikproject.domain.repository.WeeksListRepository

class WeeksListGetUseCase(private val weeksListRepository: WeeksListRepository) {
    fun execute(): List<WeeksListItem> {
        return weeksListRepository.get()
    }
}