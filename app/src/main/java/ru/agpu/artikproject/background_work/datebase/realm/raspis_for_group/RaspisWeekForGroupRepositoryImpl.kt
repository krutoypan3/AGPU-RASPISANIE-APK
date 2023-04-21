package ru.agpu.artikproject.background_work.datebase.realm.raspis_for_group

import io.realm.Realm
import io.realm.RealmConfiguration
import ru.agpu.artikproject.background_work.api.raspis_week.RaspisWeekResponse
import ru.agpu.artikproject.background_work.datebase.realm.RealmConfig

class RaspisWeekForGroupRepositoryImpl {
    private val config: RealmConfiguration = RealmConfig().providesRealmConfig()

    // Конвертируем данные из DTO в обычный Data класс, т.к. Realm классы не передаются между потоками
    private fun raspisWeekConvertToData(raspisWeekDto: RaspisWeekDto): RaspisWeek {
        return RaspisWeek(
            paraId = raspisWeekDto.paraId,
            dayOfTheWeek = raspisWeekDto.dayOfTheWeek, // podgroup.DayNumber
            paraNumber = raspisWeekDto.paraNumber,
            paraPodgroup = raspisWeekDto.paraPodgroup,
            Auditoriya = raspisWeekDto.Auditoriya,
            Gruppa = raspisWeekDto.Gruppa,
            Podgruppa = raspisWeekDto.Podgruppa,
            WeekType = raspisWeekDto.WeekType,
            DisciplineId = raspisWeekDto.DisciplineId,
            DisciplineName = raspisWeekDto.DisciplineName,
            PrepodFIO = raspisWeekDto.PrepodFIO,
            Comment = raspisWeekDto.ParaTypeColor,
            DayNumber = raspisWeekDto.DayNumber,
            TipZanyatiya = raspisWeekDto.TipZanyatiya,
            ParaNummber = raspisWeekDto.ParaNummber,
            IsCollisionClassroom = raspisWeekDto.IsCollisionClassroom,
            IsCollisionTeacher = raspisWeekDto.IsCollisionTeacher,
        )
    }

    fun getAllRaspisWeekForGroup(): List<RaspisWeek> {
        // 1. Получаем экземпляр Realm
        val realm = Realm.getInstance(config)
        val raspisWeekDtoList = mutableListOf<RaspisWeekDto>()
        // 2. // Получаем все группы
        realm.executeTransaction{ realmTransaction ->
            raspisWeekDtoList.addAll(realmTransaction
                // 3.
                .where(RaspisWeekDto::class.java)
                // 4.
                .findAll())
        }

        return raspisWeekDtoList.map { raspisWeekConvertToData(it) }
    }

    fun updateRaspisWeekForGroup(raspisWeekResponse: RaspisWeekResponse) {
        val raspisWeekDtoList = mutableListOf<RaspisWeekDto>()

        val raspisWeekList = arrayListOf(
            raspisWeekResponse.Monday, raspisWeekResponse.Tuesday, raspisWeekResponse.Wednesday,
            raspisWeekResponse.Thursday, raspisWeekResponse.Friday, raspisWeekResponse.Saturday,
            raspisWeekResponse.Sunday)

        raspisWeekList.forEachIndexed { dayNumber, paraList ->
            paraList.forEachIndexed { paraNumber, para ->

                if (para.PodgrupsInPar.isNullOrEmpty()) {
                    val raspisWeekDto = RaspisWeekDto()
                    raspisWeekDto.dayOfTheWeek = dayNumber // podgroup.DayNumber
                    raspisWeekDto.paraNumber = paraNumber

                    raspisWeekDtoList.add(raspisWeekDto)
                }
                else {
                    para.PodgrupsInPar.forEachIndexed { podgroupNumber, podgroup ->
                        val raspisWeekDto = RaspisWeekDto()
                        raspisWeekDto.paraId = podgroup.Id
                        raspisWeekDto.dayOfTheWeek = dayNumber // podgroup.DayNumber
                        raspisWeekDto.paraNumber = paraNumber
                        raspisWeekDto.paraPodgroup = podgroupNumber
                        raspisWeekDto.Auditoriya = podgroup.Auditoriya
                        raspisWeekDto.Gruppa = podgroup.Gruppa
                        raspisWeekDto.Podgruppa = podgroup.Podgruppa
                        raspisWeekDto.WeekType = podgroup.WeekType
                        raspisWeekDto.DisciplineId = podgroup.DisciplineId
                        raspisWeekDto.DisciplineName = podgroup.DisciplineName
                        raspisWeekDto.PrepodFIO = podgroup.PrepodFIO
                        raspisWeekDto.Comment = podgroup.ParaTypeColor
                        raspisWeekDto.DayNumber = podgroup.DayNumber
                        raspisWeekDto.TipZanyatiya = podgroup.TipZanyatiya
                        raspisWeekDto.ParaNummber = podgroup.ParaNummber
                        raspisWeekDto.IsCollisionClassroom = podgroup.IsCollisionClassroom
                        raspisWeekDto.IsCollisionTeacher = podgroup.IsCollisionTeacher

                        raspisWeekDtoList.add(raspisWeekDto)
                    }
                }
            }
        }

        // 1.
        val realm = Realm.getInstance(config)

        // 2.
        realm.executeTransactionAsync{ realmTransaction ->
            realmTransaction.insertOrUpdate(raspisWeekDtoList)
        }
    }
}