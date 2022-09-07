package ru.agpu.artikproject.domain.usecase

import ru.agpu.artikproject.domain.repository.TextDetranslitRepository

class TextDetranslitUseCase(private val textDetranslitRepository: TextDetranslitRepository) {
    fun execute(text: String): String {
        return textDetranslitRepository.detranslit(text = text)
    }
}