package ru.agpu.artikproject.domain.repository

interface CurrentWeekIdRepository {
    /**
     * Получение последнего номера недели сохраненного в локальной базе данных
     * @return Номер недели
     */
    fun getWeekId(): Int
}