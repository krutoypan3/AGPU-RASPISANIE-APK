package ru.agpu.artikproject.background_work.cached_file

import android.content.Context
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

/**
 * Класс, сохраняющий файл в кэш устройства.
 * @param ctx Контекст приложения
 * @param fileName Имя файла
 * @param fileType Расширение файла
 * @param url Адресс файла в сети
 * @param filesPath Путь, по которому будет сохранен файл
 */
class SaveFileToCache(
    private val ctx: Context,
    private val fileName: String,
    private val fileType: String,
    private val url: String,
    private val filesPath: String
): Thread() {
    override fun run() {
        try {
            BufferedInputStream(URL(url).openStream()).use { inputStream ->
                File(ctx.filesDir.toString() + "/" + filesPath).mkdirs()
                FileOutputStream(ctx.filesDir.toString() + "/" + filesPath + "/" + fileName + "." + fileType)
                    .use { outputStream ->
                        val data = ByteArray(1024)
                        var count: Int
                        while (inputStream.read(data, 0, 1024).also { count = it } != -1) {
                            outputStream.write(data, 0, count)
                        }
                    }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}