package ru.agpu.artikproject.data.repository.weeks_list

import android.content.ContentValues
import android.content.Context
import ru.agpu.artikproject.data.database.sqlite.DataBase
import ru.agpu.artikproject.domain.models.WeeksListItem
import java.lang.Exception

class WeeksListSetToLocal {
    fun set(context: Context, weeksList: List<WeeksListItem>){
        val rowValues = ContentValues() // Значения для вставки в базу данных

        val sqLiteDatabase = DataBase(context = context).writableDatabase
        try {
            for (weekItem in weeksList) {
                rowValues.put("week_id", weekItem.weekId)
                rowValues.put("week_s", weekItem.startDate)
                rowValues.put("week_po", weekItem.endDate)
                sqLiteDatabase.insert(DataBase.TABLE_NAME_WEEKS_LIST, null, rowValues)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            sqLiteDatabase.close()
        }
    }
}