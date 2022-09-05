package ru.agpu.artikproject.domain.usecase.groups_list

import ru.agpu.artikproject.domain.repository.GroupsListRepository

class GroupsListGetFacultiesUseCase(private val groupsListRepository: GroupsListRepository) {
    fun execute(): List<String> {
        return groupsListRepository.getFaculties()
    }
}