package ru.agpu.artikproject.background_work.datebase

data class Raspisanie(
    var groupCode: Int? = null,         // Код группы
    var weekDay: Int? = null,           // Номер дня недели
    var weekNumber: Int? = null,        // Номер недели
    var paraNumber: Int? = null,        // Номер пары
    var paraName: String? = null,       // Название пары
    var paraPrepod: String? = null,     // Имя преподователя на паре
    var paraGroup: String? = null,      // Название группы
    var paraPodgroup: String? = null,   // Название подгруппы
    var paraAud: String? = null,        // Название аудитории
    var paraRazmer: String? = null,     // Размер ячейки в расписании
    var weekDayName: String? = null,    // Название дня недели
    var weekDayDate: String? = null,    // Дата дня пары
    var searchType: String? = null,     // Тип поиска
    var lastUpdate: Long? = null,       // Время последнего обновления
    var paraColor: String? = null,      // Цвет ячейки пары
    var paraDistant: String? = null,    // Текст дистанта пары
)