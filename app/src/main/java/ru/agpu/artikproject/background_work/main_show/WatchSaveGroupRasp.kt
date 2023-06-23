package ru.agpu.artikproject.background_work.main_show

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.widget.ListView
import android.widget.TextView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems
import ru.agpu.artikproject.background_work.datebase.DataBaseSqlite.Companion.getSqliteDatabase
import ru.agpu.artikproject.presentation.layout.MainActivity


/**
 * Выводит ранее открываемых групп
 * @param context Контекст
 */
class WatchSaveGroupRasp(context: Context, widget: Boolean? = null) {
    var r: Cursor? = null
    var groupList = ArrayList<ListViewItems>()
    var groupListType = ArrayList<String>()
    var groupListId= ArrayList<String>()

    init {
        if (widget == true) {
            r = getSqliteDatabase(context).rawQuery(
                "SELECT DISTINCT r_group_code, r_group, r_search_type, r_prepod, r_aud FROM raspisanie WHERE r_group NOT NULL AND r_prepod NOT NULL AND r_search_type NOT NULL GROUP BY r_group_code",
                null
            )
            if (r?.moveToFirst() == true) {
                watch()
            }
        } else {
            val act = context as Activity
            val listview = act.findViewById<ListView>(R.id.listview)
            val result = act.findViewById<TextView>(R.id.result)
            r = getSqliteDatabase(act).rawQuery(
                "SELECT DISTINCT r_group_code, r_group, r_search_type, r_prepod, r_aud FROM raspisanie WHERE r_group NOT NULL AND r_prepod NOT NULL AND r_search_type NOT NULL GROUP BY r_group_code",
                null
            )
            if (r?.moveToFirst() == true) {
                watch()
                MainActivity.group_listed = groupList
                MainActivity.group_listed_type = groupListType.toTypedArray()
                MainActivity.group_listed_id = groupListId.toTypedArray()
            } // Вывод SELECT запроса
            act.runOnUiThread {
                try {
                    if (MainActivity.group_listed == null || r?.count == 0) {
                        result.setText(R.string.no_saved_group)
                    } else {
                        val adapter = ListViewAdapter(act.applicationContext, MainActivity.group_listed)
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
        do {
            when (r!!.getString(2)) {
                "Group" -> groupList.add(ListViewItems(r!!.getString(1).split(",")[0]
                            .replace(")", "")
                            .replace("(", "")))
                "Classroom" -> groupList.add(ListViewItems(r!!.getString(3)
                    .split(",")[r!!.getString(3).split(",").size - 1])
                )
                "Teacher" -> groupList.add(ListViewItems(r!!.getString(3).split(",")[0]))
            }
            groupListType.add(r!!.getString(2))
            groupListId.add(r!!.getString(0))
        } while (r!!.moveToNext())
    }
}