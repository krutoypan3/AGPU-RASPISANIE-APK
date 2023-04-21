package ru.agpu.artikproject.background_work.datebase.realm.raspis_for_group

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RaspisWeekDto : RealmObject() {
    @PrimaryKey
    var paraId: Int? = null                // Id пары                              NON USABLE
    var dayOfTheWeek: Int? = null          // День недели пн-вс (0..6)
    var paraNumber: Int? = null            // Номер пары (0..7) (в день проходит max 8 пар)
    var paraPodgroup: Int? = null          // Номер подгруппы пары
    var Auditoriya: String? = null         // Аудитория
    var Gruppa: String? = null             // Группа
    var Podgruppa: String? = null          // Подгруппа
    var WeekType: Int? = null              // Тип недели ???                       NON USABLE
    var DisciplineId: Int? = null          // Id дисциплины                        NON USABLE
    var DisciplineName: String? = null     // Название дисциплины
    var PrepodFIO: String? = null          // ФИО препода
    var Comment: String? = null            // Комментарий к паре
    var ParaTypeColor: String? = null      // Цвет пары (#123456)
    var DayNumber: Int? = null             // Номер дня? Это бред... а не api...   NON USABLE
    var TipZanyatiya: String? = null       // Тип занятия (лек.)
    var ParaNummber: Int? = null           // Счётовый номер пары (258)            NON USABLE
    var IsCollisionClassroom: Boolean? = null // Классная комната ???              NON USABLE
    var IsCollisionTeacher: Boolean? = null   // Классный препод  ???              NON USABLE
}