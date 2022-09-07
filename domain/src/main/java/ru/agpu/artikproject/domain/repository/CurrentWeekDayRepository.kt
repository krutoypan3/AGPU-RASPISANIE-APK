package ru.agpu.artikproject.domain.repository

interface CurrentWeekDayRepository {
    fun get(): Int
}