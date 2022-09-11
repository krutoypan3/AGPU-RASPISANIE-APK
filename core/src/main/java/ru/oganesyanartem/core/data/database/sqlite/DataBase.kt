package ru.oganesyanartem.core.data.database.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Класс отвечающий за первичное создание \ подключение к локальной базе данных
 *
 * @param context Контекст приложения
 */
class DataBase
    (context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        createDB(sqLiteDatabase)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        createDB(db)
    }

    private fun createDB(db: SQLiteDatabase) {
        // Таблица со списком факультетов и групп
        try {
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS \"" + TABLE_NAME_SEMANTIC_GROUP + "\" (\n" +
                        "\t\"id\"\tTEXT,\n" +
                        "\t\"name\"\tTEXT,\n" +
                        "\t\"group_id\"\tTEXT,\n" +
                        "\t\"group_is_archive\"\tTEXT,\n" +
                        "\t\"group_number_of_students\"\tTEXT,\n" +
                        "\t\"group_name\"\tTEXT,\n" +
                        "\t\"group_is_raspis\"\tTEXT\n" +
                        ")"
            )
        } catch (ignored: Exception) {
        }
        try {
            db.execSQL(
                ("CREATE TABLE IF NOT EXISTS \"raspisanie\" (\n" +
                        "\t\"r_group_code\"\tINTEGER,\n" +
                        "\t\"r_week_day\"\tINTEGER,\n" +
                        "\t\"r_week_number\"\tINTEGER,\n" +
                        "\t\"r_para_number\"\tINTEGER,\n" +
                        "\t\"r_name\"\tTEXT,\n" +
                        "\t\"r_prepod\"\tTEXT,\n" +
                        "\t\"r_group\"\tTEXT,\n" +
                        "\t\"r_podgroup\"\tTEXT,\n" +
                        "\t\"r_aud\"\tTEXT,\n" +
                        "\t\"r_razmer\"\tINTEGER,\n" +
                        "\t\"r_week_day_name\"\tTEXT,\n" +
                        "\t\"r_week_day_date\"\tTEXT,\n" +
                        "\t\"r_search_type\"\tTEXT,\n" +
                        "\t\"r_last_update\"\tNUMERIC,\n" +
                        "\t\"r_color\"\tTEXT,\n" +
                        "\t\"r_distant\"\tTEXT\n" +
                        ")")
            )
        } catch (ignored: Exception) {
        }
        // База данных с избранным расписанием
        try {
            db.execSQL(
                ("CREATE TABLE IF NOT EXISTS \"rasp_update\" (\n" +
                        "\t\"r_group_code\"\tINTEGER,\n" +
                        "\t\"r_selectedItem_type\"\tTEXT,\n" +
                        "\t\"r_selectedItem\"\tTEXT\n" +
                        ")")
            )
        } catch (ignored: Exception) {
        }
        // Таблица с неделями
        try {
            db.execSQL(
                ("CREATE TABLE IF NOT EXISTS \"weeks_list\" (\n" +
                        "\t\"week_id\"\tTEXT,\n" +
                        "\t\"week_s\"\tTEXT,\n" +
                        "\t\"week_po\"\tTEXT\n" +
                        ")")
            )
        } catch (ignored: Exception) {
        }
        // Таблица с группами
        try {
            db.execSQL(
                ("CREATE TABLE IF NOT EXISTS \"groups_list\" (\n" +
                        "\t\"faculties_name\"\tTEXT,\n" +
                        "\t\"faculties_group_name\"\tTEXT,\n" +
                        "\t\"faculties_group_id\"\tTEXT\n" +
                        ")")
            )
        } catch (ignored: Exception) {
        }
        // Таблица с направлениями и их группами
        try {
            db.execSQL(
                ("CREATE TABLE IF NOT EXISTS \"directions_list\" (\n" +
                        "\t\"direction_name\"\tTEXT,\n" +
                        "\t\"group_name\"\tTEXT\n)")
            )
        } catch (ignored: Exception) {
        }
    }

    companion object {
        private const val DATABASE_NAME = "raspisanie.db"
        const val TABLE_NAME_SEMANTIC_GROUP = "semantic_group"
        const val TABLE_NAME_WEEKS_LIST = "weeks_list"
        private const val DATABASE_VERSION = 8
    }
}