package ru.agpu.artikproject.background_work.main_show

import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems
import ru.agpu.artikproject.background_work.datebase.AppData.Groups.groupListed
import ru.agpu.artikproject.background_work.datebase.AppData.Groups.groupListedId
import ru.agpu.artikproject.background_work.datebase.AppData.Groups.groupListedType
import ru.agpu.artikproject.background_work.datebase.Raspisanie
import ru.agpu.artikproject.background_work.datebase.RaspisanieRepository


/**
 * Выводит ранее открываемых групп
 */
class WatchSaveGroupRasp(widget: Boolean? = null) {
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
            if (raspisanie.isNotEmpty()) {
                watch()
                groupListed = groupList
                groupListedType = groupListType.toTypedArray()
                groupListedId = groupListId.toTypedArray()
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