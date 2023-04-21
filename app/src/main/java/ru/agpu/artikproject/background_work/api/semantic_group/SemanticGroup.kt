package ru.agpu.artikproject.background_work.api.semantic_group

data class SemanticGroup(
    val Id: Int,                // ID группы
    val IsArchive: Boolean,     // Группа в архиве
    val NumberOfStudents: Int,  // Количество студентов
    val Name: String,           // Название интернета
    val IsRaspis: Int           //
)