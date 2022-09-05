package ru.agpu.artikproject.domain.repository

import ru.agpu.artikproject.domain.models.GroupsListItem

interface GroupsListRepository {
    fun get(): List<GroupsListItem>
    fun getFaculties(): List<String>
}