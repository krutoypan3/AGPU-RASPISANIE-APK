package ru.agpu.artikproject.data.repository

import ru.agpu.artikproject.domain.repository.TextDetranslitRepository

class TextDetranslitImpl: TextDetranslitRepository {
    override fun detranslit(text: String): String  = (text).apply {
        replace(oldValue = "A", newValue = "А", ignoreCase = true)
        replace(oldValue = "B", newValue = "В", ignoreCase = true)
        replace(oldValue = "C", newValue = "С", ignoreCase = true)
        replace(oldValue = "E", newValue = "Е", ignoreCase = true)
        replace(oldValue = "H", newValue = "Н", ignoreCase = true)
        replace(oldValue = "K", newValue = "К", ignoreCase = true)
        replace(oldValue = "M", newValue = "М", ignoreCase = true)
        replace(oldValue = "X", newValue = "Х", ignoreCase = true)
        replace(oldValue = "O", newValue = "О", ignoreCase = true)
        replace(oldValue = "P", newValue = "Р", ignoreCase = true)
        replace(oldValue = "T", newValue = "Т", ignoreCase = true)
    }
}