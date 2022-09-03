package ru.agpu.artikproject.data.repository.weeks_list

import android.content.Context
import android.database.Cursor
import ru.agpu.artikproject.data.database.sqlite.DataBase
import ru.agpu.artikproject.domain.models.WeeksListItem
import java.lang.Exception

class WeeksListGetFromLocal {
    fun get(context: Context): ArrayList<WeeksListItem> {
        val weekList = ArrayList<WeeksListItem>()

        try {
            val sqLiteDatabase = DataBase(context).writableDatabase
            val r: Cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM ${DataBase.TABLE_NAME_WEEKS_LIST}",
                null
            )
            while (r.moveToNext()) {
                val weeksListItem = WeeksListItem(
                    weekId = r.getString(0).toInt(),
                    startDate = r.getString(1),
                    endDate = r.getString(2)
                )
                weekList.add(weeksListItem)
            }
            return weekList
        } catch (e: Exception) {
            e.printStackTrace()
            return weekList
        }
    }
}