package ru.oganesyanartem.core.domain.usecase

import ru.oganesyanartem.core.domain.repository.CurrentWeekDayRepository
/**
 * Получить текущий день недели
 * @return День недели 0..5(пн-сб), -1(вс \ заменяется на субботу)
 */
class CurrentWeekDayGetUseCase(private val currentWeekDayRepository: CurrentWeekDayRepository) {
    fun execute(): Int = currentWeekDayRepository.get()
}