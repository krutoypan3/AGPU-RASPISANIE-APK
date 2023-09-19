package ru.agpu.artikproject.background_work

import android.app.Activity
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.Buildings
import ru.agpu.artikproject.background_work.datebase.BuildingsRepository
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentBuildingInfo
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.LoadBuildingsList
import java.util.Arrays

class GetCorpFromAudNumber {
    fun getCorp(aud: String): Buildings? {
        val building = BuildingsRepository().getAll().firstOrNull {
            aud in (it.buildingAudiences?: emptyList())
        }
        return building
    }
}
