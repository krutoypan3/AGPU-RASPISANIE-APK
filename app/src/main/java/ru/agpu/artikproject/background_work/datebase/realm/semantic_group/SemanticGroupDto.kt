package ru.agpu.artikproject.background_work.datebase.realm.semantic_group

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SemanticGroupDto : RealmObject() {
    var facultyId: Int? = null          // Id факультета
    var facultyName: String? = null     // Название факультета

    @PrimaryKey
    var groupId: Int? = null            // Id группы
    var groupName: String? = null       // Название группы
    var numberOfStudents: Int? = null   // Кол-во студентов
    var isArchive: Boolean? = null      // Группа в архиве
    var isRaspis: Int? = null           //
}