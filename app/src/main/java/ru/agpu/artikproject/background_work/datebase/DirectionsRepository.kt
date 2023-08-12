package ru.agpu.artikproject.background_work.datebase

import android.content.ContentValues
import android.database.Cursor
import androidx.core.database.getStringOrNull

class DirectionsRepository: BaseRepository() {
    /**
     * Возвращает список всех направлений
     */
    fun getAll(): List<Directions> {
        return withSQLiteDatabase {
            it.rawQuery(
                "SELECT * FROM ${DataBaseSqlite.TABLE_DIRECTIONS}",
                null
            ).toData()
        } ?: emptyList()
    }

    fun saveDirections(directions: Directions) {
        saveDirections(listOf(directions))
    }
    fun saveDirections(directions: List<Directions>) {
        directions.putToDB()
    }

    private fun List<Directions>.putToDB() {
        withSQLiteDatabase { sqlDatabase ->
            forEach { directions ->
                val rowValues = ContentValues()
                rowValues.put(DirectionsEntry.COLUMN_DIRECTION_NAME, directions.directionName)
                rowValues.put(DirectionsEntry.COLUMN_GROUP_NAME, directions.groupName)
                sqlDatabase.insert(DataBaseSqlite.TABLE_DIRECTIONS, null, rowValues)
            }
        }
    }

    private fun Cursor.toData(): List<Directions> {
        val raspisanie = mutableListOf<Directions>()
        while (this.moveToNext()) {
            raspisanie.add(
                Directions(
                    directionName = getStringOrNull(getColumnIndex(DirectionsEntry.COLUMN_DIRECTION_NAME)),
                    groupName = getStringOrNull(getColumnIndex(DirectionsEntry.COLUMN_GROUP_NAME))
                )
            )
        }
        return raspisanie
    }
}