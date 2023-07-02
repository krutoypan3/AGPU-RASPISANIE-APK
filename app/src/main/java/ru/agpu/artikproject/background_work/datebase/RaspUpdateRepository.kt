package ru.agpu.artikproject.background_work.datebase

import android.content.ContentValues
import android.database.Cursor
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
                "r_group_code = '${raspUpdate.groupCode}' AND r_search_type = " +
                "'${raspUpdate.searchType}' AND r_para_name = '${raspUpdate.paraName}'", null
            )
        }
    }

    fun getAll(): List<RaspUpdate> {
        return withSQLiteDatabase {
            it.rawQuery("SELECT * FROM ${DataBaseSqlite.TABLE_RASP_UPDATE}", null).toData()
        } ?: emptyList()
    }

    fun getByParams(raspUpdate: RaspUpdate): List<RaspUpdate> {
        val queryBuilder = StringBuilder("SELECT * FROM ${DataBaseSqlite.TABLE_RASP_UPDATE} WHERE")
        raspUpdate.groupCode?.let { queryBuilder.append(" r_group_code = '$it' AND") }
        raspUpdate.searchType?.let { queryBuilder.append(" r_search_type = '$it' AND") }
        raspUpdate.paraName?.let { queryBuilder.append(" r_para_name = '$it' AND") }
        val queryString = queryBuilder.toString().removeSuffix("AND")
        return withSQLiteDatabase {
            it.rawQuery(queryString, null).toData()
        } ?: emptyList()
    }


    private fun RaspUpdate.putToDB() {
        withSQLiteDatabase {
            val rowValues = ContentValues()
            rowValues.put("r_group_code", groupCode)
            rowValues.put("r_search_type", searchType)
            rowValues.put("r_para_name", paraName)
            it.insert(DataBaseSqlite.TABLE_RASP_UPDATE, null, rowValues)
        }
    }

    private fun Cursor.toData(): List<RaspUpdate> {
        val raspUpdate = mutableListOf<RaspUpdate>()
        while (this.moveToNext()) {
            raspUpdate.add(
                RaspUpdate(
                    groupCode = getInt(0),
                    searchType = getString(1),
                    paraName = getString(2),
                )
            )
        }
        return raspUpdate
    }
}