package ru.agpu.artikproject.background_work.api.raspis_week

data class Podgroup(
    val Id: Int,
    val Auditoriya: String,
    val Gruppa: String,
    val WeekType: Int,
    val Podgruppa: String,
    val DisciplineId: Int,
    val DisciplineName: String,
    val PrepodFIO: String,
    val Comment: String,
    val ParaTypeColor: String,
    val DayNumber: Int,
    val TipZanyatiya: String,
    val ParaNummber: Int,
    val IsCollisionClassroom: Boolean,
    val IsCollisionTeacher: Boolean,
)