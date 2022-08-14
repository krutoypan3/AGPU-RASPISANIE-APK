package ru.agpu.artikproject.background_work.cached_file;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class SaveFileToCache extends Thread{
    Context ctx;
    String file_name;
    String file_type;
    String url;
    String FILES_PATH;

    /**
     * Класс, сохраняющий файл в кэш устройства.
     * @param ctx Контекст приложения
     * @param file_name Имя файла
     * @param file_type Расширение файла
     * @param url Адресс файла в сети
     * @param files_path Путь, по которому будет сохранен файл
     */
    public SaveFileToCache(Context ctx, String file_name, String file_type, String url, String files_path) {
        this.ctx = ctx;
        this.file_name = file_name;
        this.file_type = file_type;
        this.url = url;
        FILES_PATH = files_path;
    }

    @Override
    public void run(){
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream())) {
            new File(ctx.getFilesDir() + "/" + FILES_PATH).mkdirs();
            try (FileOutputStream fout = new FileOutputStream(
                    ctx.getFilesDir() + "/" + FILES_PATH + "/" + file_name + "." + file_type)) {
                final byte[] data = new byte[1024];
                int count;
                while ((count = in.read(data, 0, 1024)) != -1) {
                    fout.write(data, 0, count);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
