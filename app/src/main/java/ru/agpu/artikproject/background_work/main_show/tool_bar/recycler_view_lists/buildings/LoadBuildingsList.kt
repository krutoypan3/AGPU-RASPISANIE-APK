package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings

import android.app.Activity
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems
import ru.agpu.artikproject.background_work.datebase.BuildingsRepository

class LoadBuildingsList(val act: Activity): Thread() {

    companion object {
        var buildings_list = listOf<RecyclerViewItems>()
    }

    override fun run() {
        val list: MutableList<RecyclerViewItems> = ArrayList()
        val buildingsRepository = BuildingsRepository()

        val buildingsList = buildingsRepository.getAll()

        buildingsList.forEach {
            list.add(
                RecyclerViewItems(
                    it.buildingName + " по " + it.buildingAddress,
                    it.buildingsPhotosUrl?.firstOrNull() ?: "https://cdn.vectorstock.com/i/preview-1x/82/99/no-image-available-like-missing-picture-vector-43938299.jpg",
                    it.buildingAudiences?.joinToString(", ") ?: "В здании нет аудиторий"
                )
            )
        }
        buildings_list = list
    }
}