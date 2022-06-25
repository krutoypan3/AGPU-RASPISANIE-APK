package ru.agpu.artikproject.background_work.rasp_show;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.widget.CheckBox;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.datebase.DataBase_Local;
import ru.agpu.artikproject.layout.MainActivity;

public class RaspUpdateCheckBoxListener extends Thread{
    final Activity activity;
    public RaspUpdateCheckBoxListener(Activity activity){
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            Cursor sss = DataBase_Local.sqLiteDatabase.rawQuery("SELECT r_group_code FROM rasp_update WHERE r_group_code = '" + MainActivity.selectedItem_id + "'", null);
            CheckBox checkBox = activity.findViewById(R.id.checkBox);
            checkBox.setChecked(sss.getCount() != 0);
            checkBox.setOnClickListener(v -> {
                if (checkBox.isChecked()) {
                    checkBox.setTextColor(Color.GREEN);
                    ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
                    rowValues.put("r_group_code", MainActivity.selectedItem_id);
                    rowValues.put("r_selectedItem_type", MainActivity.selectedItem_type);
                    rowValues.put("r_selectedItem", MainActivity.selectedItem);
                    DataBase_Local.sqLiteDatabase.insert("rasp_update", null, rowValues);
                } else {
                    checkBox.setTextColor(Color.GRAY);
                    DataBase_Local.sqLiteDatabase.delete("rasp_update", "r_group_code = '" + MainActivity.selectedItem_id + "'", null);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
