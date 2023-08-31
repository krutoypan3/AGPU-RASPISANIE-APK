package ru.agpu.artikproject.background_work.datebase

import android.content.ContentValues
import android.database.Cursor
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import java.util.Date
import kotlin.text.StringBuilder

class RaspisanieRepository: BaseRepository() {
    fun getByGroupCodeAndWeekNumberAndWeekDay(groupCode: Int?, weekId: Int?, weekDay: Int?): List<Raspisanie> {
        return withSQLiteDatabase {
            val r = it.rawQuery(
                "SELECT * FROM ${DataBaseSqlite.TABLE_RASPISANIE} WHERE r_group_code = $groupCode AND r_week_number = " +
                        "$weekId AND r_week_day = $weekDay ORDER BY r_para_number", null
            )
            return@withSQLiteDatabase r.toData()
        } ?: emptyList()
    }

    /**
     * Возвращает список всех не пустых пар
     */
    fun getAll(): List<Raspisanie> {
        return withSQLiteDatabase {
            it.rawQuery(
                "SELECT * FROM ${DataBaseSqlite.TABLE_RASPISANIE} " +
                        "WHERE ${RaspisanieEntry.COLUMN_GROUP} NOT NULL " +
                        "AND ${RaspisanieEntry.COLUMN_PREPOD} NOT NULL " +
                        "AND ${RaspisanieEntry.COLUMN_SEARCH_TYPE} NOT NULL " +
                        "GROUP BY ${RaspisanieEntry.COLUMN_GROUP_CODE}",
                null
            ).toData()
        } ?: emptyList()
    }

    fun getParaByParams(raspisanie: Raspisanie): List<Raspisanie> {
        val sqlQueryBuilder = StringBuilder("SELECT * FROM ${DataBaseSqlite.TABLE_RASPISANIE} WHERE")

        raspisanie.groupCode?.let { sqlQueryBuilder.append(" ${RaspisanieEntry.COLUMN_GROUP_CODE} = '$it' AND") }
        raspisanie.weekDay?.let { sqlQueryBuilder.append(" ${RaspisanieEntry.COLUMN_WEEK_DAY} = '$it' AND") }
        raspisanie.weekNumber?.let { sqlQueryBuilder.append(" ${RaspisanieEntry.COLUMN_WEEK_NUMBER} = '$it' AND") }
        raspisanie.paraNumber?.let { sqlQueryBuilder.append(" ${RaspisanieEntry.COLUMN_PARA_NUMBER} = '$it' AND") }
        raspisanie.paraName?.let { sqlQueryBuilder.append(" ${RaspisanieEntry.COLUMN_NAME} = '$it' AND") }
        raspisanie.paraPrepod?.let { sqlQueryBuilder.append(" ${RaspisanieEntry.COLUMN_PREPOD} = '$it' AND") }
        raspisanie.paraGroup?.let { sqlQueryBuilder.append(" ${RaspisanieEntry.COLUMN_GROUP} = '$it' AND") }
        raspisanie.paraPodgroup?.let { sqlQueryBuilder.append(" ${RaspisanieEntry.COLUMN_PODGROUP} = '$it' AND") }
        raspisanie.paraAud?.let { sqlQueryBuilder.append(" ${RaspisanieEntry.COLUMN_AUD} = '$it' AND") }
        raspisanie.paraRazmer?.let { sqlQueryBuilder.append(" ${RaspisanieEntry.COLUMN_RAZMER} = '$it' AND") }
        raspisanie.weekDayName?.let { sqlQueryBuilder.append(" ${RaspisanieEntry.COLUMN_WEEK_DAY_NAME} = '$it' AND") }
        raspisanie.weekDayDate?.let { sqlQueryBuilder.append(" ${RaspisanieEntry.COLUMN_WEEK_DAY_DATE} = '$it' AND") }

        return withSQLiteDatabase {
            it.rawQuery(sqlQueryBuilder.toString().removeSuffix("AND"), null).toData()
        } ?: emptyList()
    }

    fun deletePara(raspisanie: Raspisanie) {

        val queryWhereClause = StringBuilder()
        raspisanie.groupCode?.let { queryWhereClause.append(" ${RaspisanieEntry.COLUMN_GROUP_CODE} = '$it' AND") }
        raspisanie.weekNumber?.let { queryWhereClause.append(" ${RaspisanieEntry.COLUMN_WEEK_NUMBER} = '$it' AND") }
        raspisanie.weekDay?.let { queryWhereClause.append(" ${RaspisanieEntry.COLUMN_WEEK_DAY} = '$it' AND") }
        raspisanie.paraNumber?.let { queryWhereClause.append(" ${RaspisanieEntry.COLUMN_PARA_NUMBER} = '$it' AND") }
        raspisanie.searchType?.let { queryWhereClause.append(" ${RaspisanieEntry.COLUMN_SEARCH_TYPE} = '$it' AND") }

        withSQLiteDatabase {
            it.delete(
                DataBaseSqlite.TABLE_RASPISANIE, queryWhereClause.toString().removeSuffix("AND"), null
            )
        }
    }

    fun deleteAll() {
        withSQLiteDatabase {
            it.execSQL("DELETE FROM ${DataBaseSqlite.TABLE_RASPISANIE}")
        }
    }

    fun deletePara(raspisanie: List<Raspisanie>) {
        raspisanie.forEach { deletePara(it) }
    }

    fun saveRaspisanie(raspisanie: Raspisanie) {
        saveRaspisanie(listOf(raspisanie))
    }
    fun saveRaspisanie(raspisanie: List<Raspisanie>) {
        raspisanie.putToDB()
    }

    private fun List<Raspisanie>.putToDB() {
        withSQLiteDatabase { sqlDatabase ->
            forEach { raspisanie ->
                val rowValues = ContentValues()
                rowValues.put(RaspisanieEntry.COLUMN_GROUP_CODE, raspisanie.groupCode)
                rowValues.put(RaspisanieEntry.COLUMN_WEEK_DAY, raspisanie.weekDay)
                rowValues.put(RaspisanieEntry.COLUMN_WEEK_NUMBER, raspisanie.weekNumber)
                rowValues.put(RaspisanieEntry.COLUMN_PARA_NUMBER, raspisanie.paraNumber)
                rowValues.put(RaspisanieEntry.COLUMN_NAME, raspisanie.paraName)
                rowValues.put(RaspisanieEntry.COLUMN_PREPOD, raspisanie.paraPrepod)
                rowValues.put(RaspisanieEntry.COLUMN_GROUP, raspisanie.paraGroup)
                rowValues.put(RaspisanieEntry.COLUMN_PODGROUP, raspisanie.paraPodgroup)
                rowValues.put(RaspisanieEntry.COLUMN_AUD, raspisanie.paraAud)
                rowValues.put(RaspisanieEntry.COLUMN_RAZMER, raspisanie.paraRazmer)
                rowValues.put(RaspisanieEntry.COLUMN_WEEK_DAY_NAME, raspisanie.weekDayName)
                rowValues.put(RaspisanieEntry.COLUMN_WEEK_DAY_DATE, raspisanie.weekDayDate)
                rowValues.put(RaspisanieEntry.COLUMN_SEARCH_TYPE, raspisanie.searchType)
                rowValues.put(RaspisanieEntry.COLUMN_LAST_UPDATE, Date().time)
                rowValues.put(RaspisanieEntry.COLUMN_COLOR, raspisanie.paraColor)
                rowValues.put(RaspisanieEntry.COLUMN_DISTANT, raspisanie.paraDistant)
                sqlDatabase.insert(DataBaseSqlite.TABLE_RASPISANIE, null, rowValues)
            }
        }
    }

    private fun Cursor.toData(): List<Raspisanie> {
        val raspisanie = mutableListOf<Raspisanie>()
        while (this.moveToNext()) {
            raspisanie.add(Raspisanie(
                groupCode = getIntOrNull(getColumnIndex(RaspisanieEntry.COLUMN_GROUP_CODE)),
                weekDay = getIntOrNull(getColumnIndex(RaspisanieEntry.COLUMN_WEEK_DAY)),
                weekNumber = getIntOrNull(getColumnIndex(RaspisanieEntry.COLUMN_WEEK_NUMBER)),
                paraNumber = getIntOrNull(getColumnIndex(RaspisanieEntry.COLUMN_PARA_NUMBER)),
                paraName = getStringOrNull(getColumnIndex(RaspisanieEntry.COLUMN_NAME)),
                paraPrepod = getStringOrNull(getColumnIndex(RaspisanieEntry.COLUMN_PREPOD)),
                paraGroup = getStringOrNull(getColumnIndex(RaspisanieEntry.COLUMN_GROUP)),
                paraPodgroup = getStringOrNull(getColumnIndex(RaspisanieEntry.COLUMN_PODGROUP)),
                paraAud = getStringOrNull(getColumnIndex(RaspisanieEntry.COLUMN_AUD)),
                paraRazmer = getStringOrNull(getColumnIndex(RaspisanieEntry.COLUMN_RAZMER)),
                weekDayName = getStringOrNull(getColumnIndex(RaspisanieEntry.COLUMN_WEEK_DAY_NAME)),
                weekDayDate = getStringOrNull(getColumnIndex(RaspisanieEntry.COLUMN_WEEK_DAY_DATE)),
                searchType = getStringOrNull(getColumnIndex(RaspisanieEntry.COLUMN_SEARCH_TYPE)),
                lastUpdate = getLongOrNull(getColumnIndex(RaspisanieEntry.COLUMN_LAST_UPDATE)),
                paraColor = getStringOrNull(getColumnIndex(RaspisanieEntry.COLUMN_COLOR)),
                paraDistant = getStringOrNull(getColumnIndex(RaspisanieEntry.COLUMN_DISTANT)),
            ))
        }
        return raspisanie
    }
}