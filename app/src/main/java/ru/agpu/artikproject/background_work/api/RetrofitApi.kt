package ru.agpu.artikproject.background_work.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.agpu.artikproject.background_work.api.raspis_week.RaspisForTwoWeeksResponse
import ru.agpu.artikproject.background_work.api.raspis_week.RaspisWeekResponse
import ru.agpu.artikproject.background_work.api.semantic_group.SemanticGroupResponse

interface RetrofitApi {
    @GET("GetSemanticGroup")
    fun getSemanticGroup(@Query("ClientId") ClientId: Int): Single<List<SemanticGroupResponse>>

    /**
     * Получить расписание группы или преподавателя на неделю
     */
    @GET("GetRaspisForGroup")
    fun getRaspisForGroup(
        @Query("GroupId") GroupId: Int,
        @Query("OwnerId") OwnerId: Int,
        @Query("RequestType") RequestType: String,
    ): Single<RaspisWeekResponse>

    /**
     * Получить расписание группы или преподавателя на две недели (+_+)
     */
    @GET("GetRaspisForGroupForTwoWeeks")
    fun getRaspisForGroupForTwoWeeks(
        @Query("GroupId") GroupId: Int,
        @Query("OwnerId") OwnerId: Int,
        @Query("RequestType") RequestType: String,
    ): Single<List<RaspisForTwoWeeksResponse>>
}