package ru.agpu.artikproject.background_work.datebase

import android.content.ContentValues
import android.database.Cursor
import java.util.Date


class RaspisanieRepository {
    fun getAll(): List<Raspisanie> {
        return TODO()
    }

    fun getByGroupCodeAndWeekNumberAndWeekDay(groupCode: Int?, weekId: Int?, weekDay: Int?): List<Raspisanie> {
        val r = DataBaseSqlite.getSqliteDatabase().rawQuery(
            "SELECT * FROM raspisanie WHERE r_group_code = $groupCode AND r_week_number = " +
                    "$weekId AND r_week_day = $weekDay ORDER BY r_para_number", null
        )
        return r.toData()
    }

    fun getParaByParams(groupCode: Int?,
                     weekNumber: Int?,
                     weekDay: Int?,
                     paraNumber: Int?,
                     searchType: String?): List<Raspisanie> {
        val r = DataBaseSqlite.getSqliteDatabase().rawQuery(
            "SELECT * FROM raspisanie WHERE r_group_code = $groupCode AND r_week_number = " +
                    "$weekNumber AND r_week_day = $weekDay AND r_para_number = $paraNumber AND" +
                    " r_search_type = '$searchType'",
            null
        )
        return r.toData()
    }

    fun deleteParaByParams(groupCode: Int?,
                           weekNumber: Int?,
                           weekDay: Int?,
                           paraNumber: Int?,
                           searchType: String?) {
        DataBaseSqlite.getSqliteDatabase().delete("raspisanie", "r_group_code = $groupCode AND r_week_number = " +
                    "$weekNumber AND r_week_day = $weekDay AND r_para_number = $paraNumber AND" +
                    " r_search_type = '$searchType'", null);
    }

    fun deletePara(raspisanie: Raspisanie) {
        deleteParaByParams(
            groupCode = raspisanie.groupCode,
            weekNumber = raspisanie.weekNumber,
            weekDay = raspisanie.weekDay,
            paraNumber = raspisanie.paraNumber,
            searchType = raspisanie.searchType,
        )
    }

    fun deletePara(raspisanie: List<Raspisanie>) {
        raspisanie.forEach { deletePara(it) }
    }

    fun saveRaspisanie(raspisanie: Raspisanie) {
        saveRaspisanie(listOf(raspisanie))
    }
    fun saveRaspisanie(raspisanie: List<Raspisanie>) {
        raspisanie.forEach {
            it.putToDB()
        }
    }

    private fun Raspisanie.putToDB() {
        DataBaseSqlite.getSqliteDatabase().use { sqLiteDatabaseS ->
            val rowValues = ContentValues()
            rowValues.put("r_group_code", groupCode)
            rowValues.put("r_week_day", weekDay)
            rowValues.put("r_week_number", weekNumber)
            rowValues.put("r_para_number", paraNumber)
            rowValues.put("r_name", paraName)
            rowValues.put("r_prepod", paraPrepod)
            rowValues.put("r_group", paraGroup)
            rowValues.put("r_podgroup", paraPodgroup)
            rowValues.put("r_aud", paraAud)
            rowValues.put("r_razmer", paraRazmer)
            rowValues.put("r_week_day_name", weekDayName)
            rowValues.put("r_week_day_date", weekDayDate)
            rowValues.put("r_search_type", searchType)
            rowValues.put("r_last_update", Date().time)
            rowValues.put("r_color", paraColor)
            rowValues.put("r_distant", paraDistant)
            sqLiteDatabaseS.insert("raspisanie", null, rowValues)
        }
    }

    private fun Cursor.toData(): List<Raspisanie> {
        val raspisanie = mutableListOf<Raspisanie>()
        while (this.moveToNext()) {
            raspisanie.add(Raspisanie(
                groupCode = getInt(0),
                weekDay = getInt(1),
                weekNumber = getInt(2),
                paraNumber = getInt(3),
                paraName = getString(4),
                paraPrepod = getString(5),
                paraGroup = getString(6),
                paraPodgroup = getString(7),
                paraAud = getString(8),
                paraRazmer = getString(9),
                weekDayName = getString(10),
                weekDayDate = getString(11),
                searchType = getString(12),
                lastUpdate = getLong(13),
                paraColor = getString(14),
                paraDistant = getString(15),
            ))
        }
        return raspisanie
    }
}