package ru.agpu.artikproject.background_work.api.raspis_week

import com.google.gson.annotations.SerializedName

data class CurrentWeek(
    @SerializedName("Id")
    val Id: Int,

    @SerializedName("Name")
    val Name: String,

    @SerializedName("StartDate")
    val StartDate: String,

    @SerializedName("EndDate")
    val EndDate: String,

    @SerializedName("SemestId")
    val SemestId: Int,

    @SerializedName("OwnerId")
    val OwnerId: Int,
)