package com.example.artikproject.background_work;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.example.artikproject.background_work.datebase.DateBase_Online;
import com.example.artikproject.background_work.debug.Device_info;


public class CheckAppUpdate extends Thread {
    private final Context context;
    /**
     * Класс отвечающий за поиск обновлений приложения
     * @param context Контекст приложения
     */
    public CheckAppUpdate(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        try{
            int versionCode = Device_info.getAppVersionCode(context);
            DateBase_Online dateBase_online = new DateBase_Online();
            String[] version_info_db = dateBase_online.check_update();
            if(Integer.parseInt(version_info_db[0]) > versionCode){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Найдена новая версия приложения!")
                    .setMessage("В новой версии:\n" + version_info_db[3])
                    .setCancelable(false)
                        .setNegativeButton("Отмена", (dialog, which) -> dialog.cancel())
                    .setPositiveButton("Обновить", (dialog, which) -> {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(version_info_db[2])));
                        dialog.cancel();
                    });
                // Это нужно для вызова вне основного потока
                new Handler(Looper.getMainLooper()).post(() -> {
                    AlertDialog UpdateDialog = builder.create();
                    UpdateDialog.show();
                });
            }
            else{
                System.out.println("Новых версий не найдено.");
            }
        }
        catch (Exception e){
            System.out.println("Ошибка в модуле CheckAppUpdate");
        }
    }
}
