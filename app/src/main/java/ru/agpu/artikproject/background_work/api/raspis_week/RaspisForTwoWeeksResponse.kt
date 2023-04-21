package ru.agpu.artikproject.background_work.api.raspis_week

import com.google.gson.annotations.SerializedName

data class RaspisForTwoWeeksResponse(
    @SerializedName("WeekWithRaspis")
    val WeekWithRaspis: RaspisWeekResponse,

    @SerializedName("CurrentWeek")
    val CurrentWeek: CurrentWeek,
)