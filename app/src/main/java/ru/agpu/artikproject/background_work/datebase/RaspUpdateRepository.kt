package ru.agpu.artikproject.background_work.datebase

import android.content.ContentValues
import android.database.Cursor
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import java.lang.StringBuilder

class RaspUpdateRepository: BaseRepository() {

    fun saveRaspUpdate(raspUpdate: List<RaspUpdate>) {
        raspUpdate.forEach { saveRaspUpdate(it) }
    }

    fun saveRaspUpdate(raspUpdate: RaspUpdate) {
        raspUpdate.putToDB()
    }

    fun deleteRaspUpdate(raspUpdate: List<RaspUpdate>) {
        raspUpdate.forEach { deleteRaspUpdate(it) }
    }

    fun deleteRaspUpdate(raspUpdate: RaspUpdate) {
        withSQLiteDatabase {
            it.delete(DataBaseSqlite.TABLE_RASP_UPDATE,
                "${RaspUpdateEntry.COLUMN_GROUP_CODE} = '${raspUpdate.groupCode}' " +
                        "AND ${RaspUpdateEntry.COLUMN_SEARCH_TYPE} = '${raspUpdate.searchType}' " +
                        "AND ${RaspUpdateEntry.COLUMN_PARA_NAME} = '${raspUpdate.paraName}'",
                null
            )
        }
    }

    fun deleteAll() {
        withSQLiteDatabase {
            it.execSQL("DELETE FROM ${DataBaseSqlite.TABLE_RASP_UPDATE}")
        }
    }

    fun getAll(): List<RaspUpdate> {
        return withSQLiteDatabase {
            it.rawQuery("SELECT * FROM ${DataBaseSqlite.TABLE_RASP_UPDATE}", null).toData()
        } ?: emptyList()
    }

    fun getByParams(raspUpdate: RaspUpdate): List<RaspUpdate> {
        val queryBuilder = StringBuilder("SELECT * FROM ${DataBaseSqlite.TABLE_RASP_UPDATE} WHERE")
        raspUpdate.groupCode?.let { queryBuilder.append(" ${RaspUpdateEntry.COLUMN_GROUP_CODE} = '$it' AND") }
        raspUpdate.searchType?.let { queryBuilder.append(" ${RaspUpdateEntry.COLUMN_SEARCH_TYPE} = '$it' AND") }
        raspUpdate.paraName?.let { queryBuilder.append(" ${RaspUpdateEntry.COLUMN_PARA_NAME} = '$it' AND") }
        val queryString = queryBuilder.toString().removeSuffix("AND")
        return withSQLiteDatabase {
            it.rawQuery(queryString, null).toData()
        } ?: emptyList()
    }


    private fun RaspUpdate.putToDB() {
        withSQLiteDatabase {
            val rowValues = ContentValues()
            rowValues.put(RaspUpdateEntry.COLUMN_GROUP_CODE, groupCode)
            rowValues.put(RaspUpdateEntry.COLUMN_SEARCH_TYPE, searchType)
            rowValues.put(RaspUpdateEntry.COLUMN_PARA_NAME, paraName)
            it.insert(DataBaseSqlite.TABLE_RASP_UPDATE, null, rowValues)
        }
    }

    private fun Cursor.toData(): List<RaspUpdate> {
        val raspUpdate = mutableListOf<RaspUpdate>()
        while (this.moveToNext()) {
            raspUpdate.add(
                RaspUpdate(
                    groupCode = getIntOrNull(getColumnIndex(RaspUpdateEntry.COLUMN_GROUP_CODE)),
                    searchType = getStringOrNull(getColumnIndex(RaspUpdateEntry.COLUMN_SEARCH_TYPE)),
                    paraName = getStringOrNull(getColumnIndex(RaspUpdateEntry.COLUMN_PARA_NAME)),
                )
            )
        }
        return raspUpdate
    }
}