package ru.agpu.artikproject.domain.repository

interface TextDetranslitRepository {

    fun detranslit(text: String): String
}