package ru.agpu.artikproject.background_work.main_show

import android.app.Activity
import android.content.Context
import android.widget.ListView
import android.widget.TextView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems
import ru.agpu.artikproject.background_work.datebase.Raspisanie
import ru.agpu.artikproject.background_work.datebase.RaspisanieRepository
import ru.agpu.artikproject.presentation.layout.MainActivity


/**
 * Выводит ранее открываемых групп
 * @param context Контекст
 */
class WatchSaveGroupRasp(context: Context, widget: Boolean? = null) {
    var raspisanie: List<Raspisanie> = emptyList()
    var groupList = ArrayList<ListViewItems>()
    var groupListType = ArrayList<String>()
    var groupListId= ArrayList<String>()

    init {
        val raspisanieRepository = RaspisanieRepository()
        raspisanie = raspisanieRepository.getAll()
        if (widget == true) {
            if (raspisanie.isNotEmpty()) {
                watch()
            }
        } else {
            val act = context as Activity
            val listview = act.findViewById<ListView>(R.id.listview)
            val result = act.findViewById<TextView>(R.id.result)

            if (raspisanie.isNotEmpty()) {
                watch()
                MainActivity.group_listed = groupList
                MainActivity.group_listed_type = groupListType.toTypedArray()
                MainActivity.group_listed_id = groupListId.toTypedArray()
            }
            act.runOnUiThread {
                try {
                    if (MainActivity.group_listed == null || raspisanie.isEmpty()) {
                        result.setText(R.string.no_saved_group)
                    } else {
                        val adapter =
                            ListViewAdapter(act.applicationContext, MainActivity.group_listed)
                        listview.adapter = adapter
                        result.text = ""
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun watch() {
        groupList = ArrayList()
        groupListType = ArrayList()
        groupListId = ArrayList()
        raspisanie.forEach { r ->
            when (r.searchType) {
                "Group" -> groupList.add(ListViewItems(r.paraGroup!!
                        .split(",")[0]
                        .replace(")", "")
                        .replace("(", "")))
                "Classroom" -> groupList.add(ListViewItems(r.paraPrepod!!.split(",").last()))
                "Teacher" -> groupList.add(ListViewItems(r.paraPrepod!!.split(",").first()))
            }
            groupListType.add(r.searchType!!)
            groupListId.add(r.groupCode!!.toString())
        }
    }
}