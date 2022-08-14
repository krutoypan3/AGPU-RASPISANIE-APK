package ru.agpu.artikproject.background_work.cached_file;

import android.content.Context;

import java.io.File;

public class CheckFileInCache {
    /**
     * Проверяет наличие файла в кэше приложения
     * @param ctx Контекст
     * @param FILE_PATH Путь к файлу
     * @return True \ False
     */
    public boolean check(Context ctx, String FILE_PATH) {
        return new File(ctx.getFilesDir() + FILE_PATH).exists();
    }
}
