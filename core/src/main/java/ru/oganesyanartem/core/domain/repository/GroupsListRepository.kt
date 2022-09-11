package ru.oganesyanartem.core.domain.repository

import ru.oganesyanartem.core.domain.models.GroupsListItem

interface GroupsListRepository {
    fun get(): List<GroupsListItem>
    fun getFaculties(): List<String>
}