package ru.oganesyanartem.core.domain.usecase.weeks_list

import ru.oganesyanartem.core.domain.models.WeeksListItem
import ru.oganesyanartem.core.domain.repository.WeeksListRepository

class WeeksListGetUseCase(private val weeksListRepository: WeeksListRepository) {
    fun execute(): List<WeeksListItem> {
        return weeksListRepository.get()
    }
}