package ru.agpu.artikproject.background_work.main_show.fragments

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewAdapter
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.LoadBuildingsList
import ru.oganesyanartem.core.data.repository.groups_list.GroupsListImpl
import ru.oganesyanartem.core.domain.repository.GroupsListRepository
import ru.oganesyanartem.core.domain.usecase.groups_list.GroupsListGetFacultiesUseCase

class FragmentRecyclerviewShow: Fragment(R.layout.fragment_main_activity_recyclerview_show) {
    companion object {
        var SELECTED_LIST = 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = view.context as Activity

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)

        val groupsListRepository: GroupsListRepository = GroupsListImpl(requireContext())
        val groupsListItems = GroupsListGetFacultiesUseCase(groupsListRepository).execute()

        val facultiesListRVItems: MutableList<RecyclerViewItems> = ArrayList()

        for (i in groupsListItems.indices) {
            val facultiesName = groupsListItems[i]
            val ico = if ("Институт прикладной информатики" in facultiesName) "https://i.ibb.co/j33KDk0/agpu-ico-ipimif.png"
            else if ("Институт русской" in facultiesName) "https://i.ibb.co/K6xBNY0/agpu-ico-iriif.png"
            else if ("Исторический факультет" in facultiesName) "https://i.ibb.co/z42VmXz/agpu-ico-istfak.png"
            else if ("Социально-психологический факультет" in facultiesName) "https://i.ibb.co/FzXkFdy/agpu-ico-spf.png"
            else if ("Факультет дошкольного" in facultiesName) "https://i.ibb.co/hWQR2CL/agpu-ico-fdino.png"
            else if ("Факультет технологии" in facultiesName) "https://i.ibb.co/gg2rsfV/agpu-ico-fteid.png"
            else {"https://i.ibb.co/PZhZjkZ/agpu-ico.png"}
            facultiesListRVItems.add(RecyclerViewItems(facultiesName, ico, ""))
        }
        when (SELECTED_LIST) {
            1 -> recyclerView.adapter = RecyclerViewAdapter(
                    activity,
                    LoadBuildingsList.buildings_list,
                    RecyclerViewAdapter.IS_BUILDINGS_ADAPTER
            )

            2 -> recyclerView.adapter = RecyclerViewAdapter(
                activity,
                facultiesListRVItems,
                RecyclerViewAdapter.IS_FACULTIES_ADAPTER
            )
        }
    }
}