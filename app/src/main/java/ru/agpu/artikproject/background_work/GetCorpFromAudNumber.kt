package ru.agpu.artikproject.background_work

import android.app.Activity
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.LoadBuildingsList
import java.util.Arrays

class GetCorpFromAudNumber {
    fun getCorp(act: Activity, aud: String): Int {
        return when {
            act.resources.getString(R.string.adress_zaochka_aud).replace(", ", ",").split(",").contains(aud) ->
                LoadBuildingsList.BUILDING_ZAOCHKA

            act.resources.getString(R.string.adress_spf_aud).replace(", ", ",").split(",").contains(aud) ->
                LoadBuildingsList.BUILDING_SPF

            act.resources.getString(R.string.adress_foc_aud).replace(", ", ",").split(",").contains(aud) ->
                LoadBuildingsList.BUILDING_FOC

            act.resources.getString(R.string.adress_tehfak_aud).replace(", ", ",").split(",").contains(aud) ->
                LoadBuildingsList.BUILDING_TEHFAK

            act.resources.getString(R.string.adress_obshaga_aud).replace(", ", ",").split(",").contains(aud) ->
                LoadBuildingsList.BUILDING_DORMITORY

            act.resources.getString(R.string.adress_istfak_aud).replace(", ", ",").split(",").contains(aud) ->
                LoadBuildingsList.BUILDING_ISTFAK

            else -> 0
        }
    }
}
