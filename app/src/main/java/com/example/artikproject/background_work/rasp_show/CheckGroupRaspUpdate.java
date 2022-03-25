package com.example.artikproject.background_work.rasp_show;

import static com.example.artikproject.layout.MainActivity.sqLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.widget.CheckBox;

import com.example.artikproject.background_work.datebase.DataBase_Local;
import com.example.artikproject.layout.MainActivity;

public class CheckGroupRaspUpdate extends Thread{
    Context context;
    CheckBox checkBox;
    public CheckGroupRaspUpdate(Context context, CheckBox checkBox){
        this.context = context;
        this.checkBox = checkBox;
    }

    @Override
    public void run() {
        Cursor sss = sqLiteDatabase.rawQuery("SELECT r_group_code FROM rasp_update WHERE r_group_code = '" + MainActivity.selectedItem_id + "'", null);
        checkBox.setChecked(sss.getCount() != 0);
        checkBox.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                checkBox.setTextColor(Color.GREEN);
                ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
                rowValues.put("r_group_code", MainActivity.selectedItem_id);
                rowValues.put("r_selectedItem_type", MainActivity.selectedItem_type);
                rowValues.put("r_selectedItem", MainActivity.selectedItem);
                sqLiteDatabase.insert("rasp_update", null, rowValues);
            } else {
                checkBox.setTextColor(Color.GRAY);
                sqLiteDatabase.delete("rasp_update", "r_group_code = '" + MainActivity.selectedItem_id + "'", null);
            }

        });

    }
}
