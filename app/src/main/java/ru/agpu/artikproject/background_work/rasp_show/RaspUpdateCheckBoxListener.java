package ru.agpu.artikproject.background_work.rasp_show;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.datebase.DataBaseSqlite;
import ru.agpu.artikproject.presentation.layout.MainActivity;

public class RaspUpdateCheckBoxListener {
    public RaspUpdateCheckBoxListener(View view) {
        try {
            Cursor sss = DataBaseSqlite.Companion.getSqliteDatabase(view.getContext()).rawQuery("SELECT r_group_code FROM rasp_update WHERE r_group_code = '" + MainActivity.selectedItem_id + "'", null);
            CheckBox checkBox = view.findViewById(R.id.checkBox);
            checkBox.setChecked(sss.getCount() != 0);
            checkBox.setOnClickListener(v -> {
                if (checkBox.isChecked()) {
                    checkBox.setTextColor(Color.GREEN);
                    ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
                    rowValues.put("r_group_code", MainActivity.selectedItem_id);
                    rowValues.put("r_selectedItem_type", MainActivity.selectedItem_type);
                    rowValues.put("r_selectedItem", MainActivity.selectedItem);
                    DataBaseSqlite.Companion.getSqliteDatabase(view.getContext()).insert("rasp_update", null, rowValues);
                } else {
                    checkBox.setTextColor(Color.GRAY);
                    DataBaseSqlite.Companion.getSqliteDatabase(view.getContext()).delete("rasp_update", "r_group_code = '" + MainActivity.selectedItem_id + "'", null);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
