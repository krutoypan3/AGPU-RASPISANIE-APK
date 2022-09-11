package ru.oganesyanartem.core.domain.repository

interface TextDetranslitRepository {

    fun detranslit(text: String): String
}