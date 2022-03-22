package com.example.artikproject.background_work.main_show;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.example.artikproject.R;
import com.example.artikproject.background_work.CheckInternetConnection;
import com.example.artikproject.background_work.CustomAlertDialog;
import com.example.artikproject.background_work.rasp_show.GetRasp;
import com.example.artikproject.layout.MainActivity;
import com.example.artikproject.layout.Raspisanie_show;

public class ListView_LongClickListener extends Thread {
    public static int position;

    public boolean listen(int position, Activity act) {
        ListView_LongClickListener.position = position;
        CustomAlertDialog cdd = new CustomAlertDialog(act,"delete_one_saved_group");
        cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
        cdd.show();
        return false;
    }
}
