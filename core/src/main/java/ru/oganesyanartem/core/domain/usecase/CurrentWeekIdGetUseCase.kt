package ru.oganesyanartem.core.domain.usecase

import ru.oganesyanartem.core.domain.repository.CurrentWeekIdRepository

class CurrentWeekIdGetUseCase(private val currentWeekIdRepository: CurrentWeekIdRepository) {
    fun execute(): Int {
        return currentWeekIdRepository.getWeekId()
    }
}