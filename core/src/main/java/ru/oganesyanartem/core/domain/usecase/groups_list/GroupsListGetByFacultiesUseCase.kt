package ru.oganesyanartem.core.domain.usecase.groups_list

import ru.oganesyanartem.core.domain.models.GroupsListItem
import ru.oganesyanartem.core.domain.repository.GroupsListRepository

class GroupsListGetByFacultiesUseCase(
    private val groupsListRepository: GroupsListRepository,
    private val filteredFacultiesName: String
) {
    fun execute(): List<GroupsListItem> {
        return groupsListRepository.get().filter { it.facultiesName == filteredFacultiesName }
    }
}