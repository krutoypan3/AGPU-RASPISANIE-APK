package ru.agpu.artikproject.background_work.cached_file

import android.content.Context
import java.io.File

class CheckFileInCache {
    /**
     * Проверяет наличие файла в кэше приложения
     * @param ctx Контекст
     * @param FILE_PATH Путь к файлу
     * @return True \ False
     */
    fun check(ctx: Context, FILE_PATH: String): Boolean {
        return File(ctx.filesDir.toString() + FILE_PATH).exists()
    }
}