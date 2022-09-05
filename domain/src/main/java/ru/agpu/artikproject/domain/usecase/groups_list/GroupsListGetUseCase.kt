package ru.agpu.artikproject.domain.usecase.groups_list

import ru.agpu.artikproject.domain.models.GroupsListItem
import ru.agpu.artikproject.domain.repository.GroupsListRepository

class GroupsListGetUseCase(private val groupsListRepository: GroupsListRepository) {
    fun execute(): List<GroupsListItem> {
        return groupsListRepository.get()
    }
}