package ru.agpu.artikproject.background_work.api


import android.util.Log

class RetrofitToRealm {
    private lateinit var service: RetrofitApi
    init {
        if (!this::service.isInitialized) {
            service = ApiServiceProvider.provideService(
                ApiData.BASE_URL,
                RetrofitApi::class.java
            )
        }
    }

    // Запрашивает список факультетов и их групп и сохраняет в базу данных
    fun loadToDatabaseSemanticGroup(): Boolean{

        // Делаем запрос на сервер
        val data = service.getSemanticGroup(ApiData.CLIENT_ID)
            .compose { upstream -> upstream }
            .onErrorResumeNext { throw Exception(it.message) }
            .blockingGet()

        // Сохраняем в базу данных
//        SemanticGroupRepositoryImpl().updateSemanticGroup(data)
        return true
    }

    /**
     * Загружает расписание с сервера для конкретной группы и сохраняет в Realm
     * @param groupId id группы
     * @return true, либо выбрасывается ошибка, которую нужно обработать
     */
    fun loadRaspisForGroup(groupId: Int): Boolean{
        val data = service.getRaspisForGroupForTwoWeeks(GroupId = groupId, OwnerId = ApiData.CLIENT_ID, RequestType = ApiData.REQUEST_TYPE_GROUP)
            .compose { upstream -> upstream }
            .onErrorResumeNext { throw Exception(it.message) }
            .blockingGet()

        data.forEach {
            it.WeekWithRaspis.let { raspisWeekResponse ->
//                RaspisWeekForGroupRepositoryImpl().updateRaspisWeekForGroup(raspisWeekResponse)
            }
        }

        return true
    }
}