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


public class CheckAppUpdate extends AsyncTask<Void, Void, Void> {
    private Context context;

    public CheckAppUpdate(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try{
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int versionCode = packageInfo.versionCode; // И ее кода
            DateBase_Online dateBase_online = new DateBase_Online();
            String[] version_info_db = dateBase_online.check_update();
            if(Integer.parseInt(version_info_db[0]) > versionCode){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Найдена новая версия приложения!")
                    .setMessage("В новой версии:\n" + version_info_db[3])
                    .setCancelable(false)
                        .setNegativeButton("Отмена",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                    .setPositiveButton("Обновить",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(version_info_db[2])));
                            dialog.cancel();
                        }
                    });
                new Handler(Looper.getMainLooper()).post(new Runnable() { // Это нужно для вызова вне основного потока
                    @Override
                    public void run() {
                        AlertDialog UpdateDialog = builder.create();
                        UpdateDialog.show();
                    }
                });
            }
            else{
                System.out.println("Новых версий не найдено.");
            }
        }
        catch (Exception e){
            System.out.println("Ошибка в модуле CheckAppUpdate");
        }
    return null;
    }
}
