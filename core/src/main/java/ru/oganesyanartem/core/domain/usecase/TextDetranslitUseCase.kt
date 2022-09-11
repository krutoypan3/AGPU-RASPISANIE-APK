package ru.oganesyanartem.core.domain.usecase

import ru.oganesyanartem.core.domain.repository.TextDetranslitRepository

class TextDetranslitUseCase(private val textDetranslitRepository: TextDetranslitRepository) {
    fun execute(text: String): String {
        return textDetranslitRepository.detranslit(text = text)
    }
}