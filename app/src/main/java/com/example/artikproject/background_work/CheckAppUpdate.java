package com.example.artikproject.background_work;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;

import com.example.artikproject.DateBaseOnline;


public class CheckAppUpdate{
    public void CheckUpdate(Context context) throws Exception {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        int versionCode = packageInfo.versionCode; // И ее кода
        DateBaseOnline dateBase_online = new DateBaseOnline();
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
            AlertDialog UpdateDialog = builder.create();
            UpdateDialog.show();
        }
    }
}
