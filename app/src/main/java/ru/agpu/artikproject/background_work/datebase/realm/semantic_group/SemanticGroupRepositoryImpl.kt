package ru.agpu.artikproject.background_work.datebase.realm.semantic_group

import io.realm.Realm
import io.realm.RealmConfiguration
import ru.agpu.artikproject.background_work.api.semantic_group.SemanticGroupResponse
import ru.agpu.artikproject.background_work.datebase.realm.RealmConfig

class SemanticGroupRepositoryImpl {
    private val config: RealmConfiguration = RealmConfig().providesRealmConfig()

    fun getAllSemanticGroup(): List<SemanticGroupDto> {
        // 1. Получаем экземпляр Realm
        val realm = Realm.getInstance(config)
        val semanticGroupDtoList = mutableListOf<SemanticGroupDto>()

        // 2. // Получаем все группы
        realm.executeTransactionAsync{ realmTransaction ->
            semanticGroupDtoList.addAll(realmTransaction
                // 3.
                .where(SemanticGroupDto::class.java)
                // 4.
                .findAll()
            )
        }
        semanticGroupDtoList.sortedWith(compareBy({ it.facultyName }, { it.groupName }))
        return semanticGroupDtoList
    }

    fun updateSemanticGroup(semanticGroupList: List<SemanticGroupResponse>) {
        val semanticGroupDtoList = mutableListOf<SemanticGroupDto>()
        semanticGroupList.forEach { semanticGroup ->
            semanticGroup.Groups.forEach {
                val semanticGroupDto = SemanticGroupDto()
                semanticGroupDto.facultyId = semanticGroup.Id
                semanticGroupDto.facultyName = semanticGroup.Name
                semanticGroupDto.groupId = it.Id
                semanticGroupDto.groupName = it.Name
                semanticGroupDto.isArchive = it.IsArchive
                semanticGroupDto.isRaspis = it.IsRaspis
                semanticGroupDto.numberOfStudents = it.NumberOfStudents
                semanticGroupDtoList.add(semanticGroupDto)
            }
        }
        // 1.
        val realm = Realm.getInstance(config)

        // 2.
        realm.executeTransactionAsync{ realmTransaction ->
            realmTransaction.insertOrUpdate(semanticGroupDtoList)
        }
    }
}