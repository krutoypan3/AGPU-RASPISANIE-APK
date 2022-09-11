package ru.oganesyanartem.core.domain.repository

interface CurrentWeekDayRepository {
    fun get(): Int
}