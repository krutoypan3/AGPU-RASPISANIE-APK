package ru.agpu.artikproject.background_work.datebase

data class Buildings(
    val buildingName: String? = null,
    val buildingAddress: String? = null,
    val buildingAddressMapUrl: String? = null,
    val buildingAudiences: List<String?>? = null,
    val buildingsPhotosUrl: List<String?>? = null,
)