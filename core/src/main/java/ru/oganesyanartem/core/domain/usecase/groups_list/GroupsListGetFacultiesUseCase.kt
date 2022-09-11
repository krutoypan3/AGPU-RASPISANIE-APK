package ru.oganesyanartem.core.domain.usecase.groups_list

import ru.oganesyanartem.core.domain.repository.GroupsListRepository

class GroupsListGetFacultiesUseCase(private val groupsListRepository: GroupsListRepository) {
    fun execute(): List<String> {
        return groupsListRepository.getFaculties()
    }
}