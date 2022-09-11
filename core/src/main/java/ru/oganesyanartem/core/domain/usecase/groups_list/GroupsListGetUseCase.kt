package ru.oganesyanartem.core.domain.usecase.groups_list

import ru.oganesyanartem.core.domain.models.GroupsListItem
import ru.oganesyanartem.core.domain.repository.GroupsListRepository

class GroupsListGetUseCase(private val groupsListRepository: GroupsListRepository) {
    fun execute(): List<GroupsListItem> {
        return groupsListRepository.get()
    }
}