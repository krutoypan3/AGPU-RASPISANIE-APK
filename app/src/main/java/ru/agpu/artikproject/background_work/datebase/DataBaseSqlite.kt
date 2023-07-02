package ru.agpu.artikproject.background_work.datebase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.lang.ref.WeakReference
import java.util.UUID

/**
 * Класс отвечающий за первичное создание \ подключение к локальной базе данных
 *
 * @param context Контекст приложения
 */
class DataBaseSqlite(val context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        createDB(sqLiteDatabase)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        createDB(db)
    }

    private fun createDB(db: SQLiteDatabase) {
        // Таблица со списком факультетов и групп
        try { // @see SemanticGroupDto
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
        try { // @see RaspisanieDto
            db.execSQL(
                ("CREATE TABLE IF NOT EXISTS \"raspisanie\" (\n" +
                        "\t\"r_group_code\"\tINTEGER,\n" +  // 0
                        "\t\"r_week_day\"\tINTEGER,\n" +  // 1
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
                        "\t\"r_last_update\"\tNUMERIC,\n" +  // 13
                        "\t\"r_color\"\tTEXT,\n" +  // 14
                        "\t\"r_distant\"\tTEXT\n" +  // 15
                        ")")
            )
        } catch (ignored: Exception) {
        }
        // База данных с избранным расписанием
        try { // @see RaspUpdate
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
        try { // @see WeeksList
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
        try { // @see GroupsList
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
        try { // @see DirectionsList
            db.execSQL(
                ("CREATE TABLE IF NOT EXISTS \"directions_list\" (\n" +
                        "\t\"direction_name\"\tTEXT,\n" +
                        "\t\"group_name\"\tTEXT\n)")
            )
        } catch (ignored: Exception) {
        }
    }

    fun init() {
        updateDbContext(context = this.context)
    }

    companion object {
        private const val DATABASE_NAME = "raspisanie.db"
        private const val DATABASE_VERSION = 8
        const val TABLE_NAME_SEMANTIC_GROUP = "semantic_group"

        private var sqLiteDatabase: SQLiteDatabase? = null
            @Synchronized get
            @Synchronized set

        var dbContext: WeakReference<Context?> = WeakReference(null)

        private fun updateDbContext(context: Context?) {
            if (dbContext.get() == null) {
                dbContext = WeakReference(context)
            }
        }

        /**
         * Это переехало в BaseRepository (Нужно будет удалить отсюда, когда все будет сделано через репозитории)
         */
        fun <T> withSQLiteDataBase(context: Context? = null, db: (sqLiteDatabase: SQLiteDatabase) -> T): T {
            updateDbContext(context)
            var sqlite: SQLiteDatabase? = null
            val hashTest = UUID.randomUUID().toString()
            try {
                println("OpenDb -> $hashTest")
                sqlite = DataBaseSqlite(context ?: dbContext.get()).writableDatabase
                return db(sqlite)
            } finally {
                println("CloseDb <- $hashTest")
                sqlite?.close()
            }
        }

        @Deprecated("Заменено на лямбда функцию withSQLiteDataBase. Kotlin only")
        @Synchronized
        fun getSqliteDatabase(context: Context? = null): SQLiteDatabase {
            sqLiteDatabase = DataBaseSqlite(context ?: dbContext.get()).writableDatabase

            if (dbContext.get() == null) {
                dbContext = WeakReference(context)
            }
            return sqLiteDatabase ?: throw Exception("Ошибка при попытке вернуть экземпляр базы данных")
        }
    }
}
