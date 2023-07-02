package ru.agpu.artikproject.background_work.datebase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.lang.Exception
import java.lang.ref.WeakReference
import java.util.UUID

open class BaseRepository {

    private fun updateDbContext(context: Context?) {
        if (DataBaseSqlite.dbContext.get() == null) {
            DataBaseSqlite.dbContext = WeakReference(context)
        }
    }

    /**
     * Лямба функция позволяющая взаимодействовать с базой данных и автоматически закрывающая подключение
     * @param context Контекст - при наличии
     */
    fun <T> withSQLiteDatabase(context: Context? = null, db: (sqLiteDatabase: SQLiteDatabase) -> T): T? {
        updateDbContext(context)
        var sqlite: SQLiteDatabase? = null
        val hashTest = UUID.randomUUID().toString()
        return try {
            println("OpenDb  -> $hashTest")
            sqlite = DataBaseSqlite(context ?: DataBaseSqlite.dbContext.get()).writableDatabase
            db(sqlite)
        } catch (e: Exception) {
            Log.e("BaseRepository", "DB error " +
                    if ((context ?: DataBaseSqlite.dbContext.get()) == null) "context is null!"
                    else "${e.message}"
            )
            null
        } finally {
            println("CloseDb <- $hashTest")
            sqlite?.close()
        }
    }
}