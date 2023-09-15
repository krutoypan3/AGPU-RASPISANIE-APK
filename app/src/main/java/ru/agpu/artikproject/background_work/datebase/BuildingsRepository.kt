package ru.agpu.artikproject.background_work.datebase

import android.content.ContentValues
import android.database.Cursor
import androidx.core.database.getStringOrNull
import java.sql.ResultSet

class BuildingsRepository: BaseRepository() {

    /**
     * Возвращает список всех строений и корпусов
     */
    fun getAll(): List<Buildings> {
        var buildingsList = withSQLiteDatabase {
            it.rawQuery("SELECT * FROM ${DataBaseSqlite.TABLE_BUILDINGS}",null).toData()
        } ?: emptyList()
        if (buildingsList.isEmpty()) {
            buildingsList = loadFromOnlineDB()
        }
        return buildingsList
    }

    fun removeAll() {
        withSQLiteDatabase {
            it.execSQL("DELETE FROM ${DataBaseSqlite.TABLE_BUILDINGS}")
        }
    }


    fun loadFromOnlineDB(): List<Buildings> {
        val statement = DataBaseOnlineHelper.getStatement()
        val valuesQuery = statement?.executeQuery("SELECT * FROM ${DataBaseSqlite.TABLE_BUILDINGS}")

        val buildingsList = valuesQuery?.toData() ?: emptyList()

        // При получении списка удаляем старый и заменяем новым
        if (buildingsList.isNotEmpty()) {
            removeAll()
        }
        saveBuildings(buildingsList)
        return buildingsList
    }

    fun saveBuildings(buildings: Buildings) {
        listOf(buildings).putToDB()
    }

    fun saveBuildings(buildings: List<Buildings>) {
        buildings.putToDB()
    }

    private fun List<Buildings>.putToDB() {
        withSQLiteDatabase { sqlDatabase ->
            forEach { buildings ->
                val rowValues = ContentValues()
                rowValues.put(BuildingsEntry.COLUMN_BUILDING_NAME, buildings.buildingName)
                rowValues.put(BuildingsEntry.COLUMN_BUILDING_ADDRESS, buildings.buildingAddress)
                rowValues.put(BuildingsEntry.COLUMN_BUILDING_ADDRESS_MAP_URL, buildings.buildingAddressMapUrl)
                rowValues.put(BuildingsEntry.COLUMN_BUILDING_AUDIENCES, buildings.buildingAudiences
                    ?.joinToString(BuildingsEntry.DB_LIST_SEPARATOR))
                rowValues.put(BuildingsEntry.COLUMN_BUILDING_PHOTOS_URL, buildings.buildingsPhotosUrl
                    ?.joinToString(BuildingsEntry.DB_LIST_SEPARATOR))
                sqlDatabase.insert(DataBaseSqlite.TABLE_BUILDINGS, null, rowValues)
            }
        }
    }

    private fun ResultSet.toData(): List<Buildings> {
        val buildingsList = mutableListOf<Buildings>()

        var haveNext = this.next()
        while (haveNext) {
            buildingsList.add(Buildings(
                buildingName = this.getString(BuildingsEntry.COLUMN_BUILDING_NAME),
                buildingAddress = this.getString(BuildingsEntry.COLUMN_BUILDING_ADDRESS),
                buildingAddressMapUrl = this.getString(BuildingsEntry.COLUMN_BUILDING_ADDRESS_MAP_URL),
                buildingAudiences = this.getString(BuildingsEntry.COLUMN_BUILDING_AUDIENCES)
                    ?.split(BuildingsEntry.DB_LIST_SEPARATOR),
                buildingsPhotosUrl = this.getString(BuildingsEntry.COLUMN_BUILDING_PHOTOS_URL)
                    ?.split(BuildingsEntry.DB_LIST_SEPARATOR),
            ))
            haveNext = this.next()
        }
        return buildingsList
    }
    private fun Cursor.toData(): List<Buildings> {
        val buildings = mutableListOf<Buildings>()
        while (this.moveToNext()) {
            buildings.add(
                Buildings(
                    buildingName = getStringOrNull(getColumnIndex(BuildingsEntry.COLUMN_BUILDING_NAME)),
                    buildingAddress = getStringOrNull(getColumnIndex(BuildingsEntry.COLUMN_BUILDING_ADDRESS)),
                    buildingAddressMapUrl = getStringOrNull(getColumnIndex(BuildingsEntry.COLUMN_BUILDING_ADDRESS_MAP_URL)),
                    buildingAudiences = getStringOrNull(getColumnIndex(BuildingsEntry.COLUMN_BUILDING_AUDIENCES))
                        ?.split(BuildingsEntry.DB_LIST_SEPARATOR),
                    buildingsPhotosUrl = getStringOrNull(getColumnIndex(BuildingsEntry.COLUMN_BUILDING_PHOTOS_URL))
                        ?.split(BuildingsEntry.DB_LIST_SEPARATOR),
                )
            )
        }
        return buildings
    }
}