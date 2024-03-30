package ru.agpu.artikproject.background_work

import ru.agpu.artikproject.background_work.datebase.Buildings
import ru.agpu.artikproject.background_work.datebase.BuildingsRepository

class GetCorpFromAudNumber {
    fun getCorp(aud: String): Buildings? {
        val building = BuildingsRepository().getAll().firstOrNull {
            aud in (it.buildingAudiences?: emptyList())
        }
        return building
    }
}
