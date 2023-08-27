package ru.agpu.artikproject.background_work.datebase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.lang.ref.WeakReference

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

        if (oldVersion == 19) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_DIRECTIONS")
            Log.i("DataBaseSqlite", "DROP TABLE $TABLE_DIRECTIONS")
        }

        createDB(db)
    }

    private fun createDB(db: SQLiteDatabase) {
        Log.e("DataBaseSqlite", "CREATE DB")
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
        } catch (e: Exception) {
            Log.e(DATABASE_NAME, "CREATE TABLE ERROR: ${e.message}")
        }
        try { // @see RaspisanieDto
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS \"$TABLE_RASPISANIE\" (\n" +
                        "\t\"${RaspisanieEntry.COLUMN_GROUP_CODE}\"\tINTEGER,\n" +  // 0
                        "\t\"${RaspisanieEntry.COLUMN_WEEK_DAY}\"\tINTEGER,\n" +  // 1
                        "\t\"${RaspisanieEntry.COLUMN_WEEK_NUMBER}\"\tINTEGER,\n" +
                        "\t\"${RaspisanieEntry.COLUMN_PARA_NUMBER}\"\tINTEGER,\n" +
                        "\t\"${RaspisanieEntry.COLUMN_NAME}\"\tTEXT,\n" +
                        "\t\"${RaspisanieEntry.COLUMN_PREPOD}\"\tTEXT,\n" +
                        "\t\"${RaspisanieEntry.COLUMN_GROUP}\"\tTEXT,\n" +
                        "\t\"${RaspisanieEntry.COLUMN_PODGROUP}\"\tTEXT,\n" +
                        "\t\"${RaspisanieEntry.COLUMN_AUD}\"\tTEXT,\n" +
                        "\t\"${RaspisanieEntry.COLUMN_RAZMER}\"\tINTEGER,\n" +
                        "\t\"${RaspisanieEntry.COLUMN_WEEK_DAY_NAME}\"\tTEXT,\n" +
                        "\t\"${RaspisanieEntry.COLUMN_WEEK_DAY_DATE}\"\tTEXT,\n" +
                        "\t\"${RaspisanieEntry.COLUMN_SEARCH_TYPE}\"\tTEXT,\n" +
                        "\t\"${RaspisanieEntry.COLUMN_LAST_UPDATE}\"\tNUMERIC,\n" +  // 13
                        "\t\"${RaspisanieEntry.COLUMN_COLOR}\"\tTEXT,\n" +  // 14
                        "\t\"${RaspisanieEntry.COLUMN_DISTANT}\"\tTEXT\n" +  // 15
                        ")"
            )
        } catch (e: Exception) {
            Log.e(DATABASE_NAME, "CREATE TABLE ERROR: ${e.message}")
        }
        // База данных с избранным расписанием
        try { // @see RaspUpdate
            db.execSQL(
                ("CREATE TABLE IF NOT EXISTS \"$TABLE_RASP_UPDATE\" (\n" +
                        "\t\"${RaspUpdateEntry.COLUMN_GROUP_CODE}\"\tINTEGER,\n" +
                        "\t\"${RaspUpdateEntry.COLUMN_SEARCH_TYPE}\"\tTEXT,\n" +
                        "\t\"${RaspUpdateEntry.COLUMN_PARA_NAME}\"\tTEXT\n" +
                        ")")
            )
        } catch (e: Exception) {
            Log.e(DATABASE_NAME, "CREATE TABLE ERROR: ${e.message}")
        }
        // Таблица с неделями
        try { // @see WeeksList
            db.execSQL(
                ("CREATE TABLE IF NOT EXISTS \"$TABLE_WEEKS_LIST\" (\n" +
                        "\t\"week_id\"\tTEXT,\n" +
                        "\t\"week_s\"\tTEXT,\n" +
                        "\t\"week_po\"\tTEXT\n" +
                        ")")
            )
        } catch (e: Exception) {
            Log.e(DATABASE_NAME, "CREATE TABLE ERROR: ${e.message}")
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
        } catch (e: Exception) {
            Log.e(DATABASE_NAME, "CREATE TABLE ERROR: ${e.message}")
        }
        // Таблица с направлениями и их группами
        try { // @see DirectionsList
            db.execSQL(
                ("CREATE TABLE IF NOT EXISTS \"$TABLE_DIRECTIONS\" (\n" +
                        "\t\"${DirectionsEntry.COLUMN_DIRECTION_NAME}\"\tTEXT,\n" +
                        "\t\"${DirectionsEntry.COLUMN_GROUP_NAME}\"\tTEXT\n)")
            )
        } catch (e: Exception) {
            Log.e(DATABASE_NAME, "CREATE TABLE ERROR: ${e.message}")
        }
    }

    fun init() {
        updateDbContext(context = this.context)
    }

    companion object {
        private const val DATABASE_NAME = "raspisanie.db"
        private const val DATABASE_VERSION = 20
        const val TABLE_NAME_SEMANTIC_GROUP = "semantic_group"

        const val TABLE_RASP_UPDATE = "rasp_update"
        const val TABLE_WEEKS_LIST = "weeks_list"
        const val TABLE_RASPISANIE = "raspisanie"
        const val TABLE_DIRECTIONS = "directions"

        var dbContext: WeakReference<Context?> = WeakReference(null)

        private fun updateDbContext(context: Context?) {
            if (dbContext.get() == null) {
                dbContext = WeakReference(context)
            }
        }
    }
}
