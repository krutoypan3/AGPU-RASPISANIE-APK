package ru.agpu.artikproject.domain.usecase

import ru.agpu.artikproject.domain.repository.CurrentWeekIdRepository

class CurrentWeekIdGetUseCase(private val currentWeekIdRepository: CurrentWeekIdRepository) {
    fun execute(): Int {
        return currentWeekIdRepository.getWeekId()
    }
}