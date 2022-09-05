package ru.agpu.artikproject.domain.usecase.groups_list

import ru.agpu.artikproject.domain.models.GroupsListItem
import ru.agpu.artikproject.domain.repository.GroupsListRepository

class GroupsListGetByFacultiesUseCase(
    private val groupsListRepository: GroupsListRepository,
    private val filteredFacultiesName: String
) {
    fun execute(): List<GroupsListItem> {
        return groupsListRepository.get().filter { it.facultiesName == filteredFacultiesName }
    }
}