package ru.agpu.artikproject.background_work.rasp_show

import android.content.ContentValues
import android.graphics.Color
import android.view.View
import android.widget.CheckBox
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.DataBaseSqlite
import ru.agpu.artikproject.presentation.layout.MainActivity

class RaspUpdateCheckBoxListener(view: View?) {
    init {
        try {
            DataBaseSqlite.withSQLiteDataBase(view?.context) { sqLiteDatabase ->
                val sss = sqLiteDatabase.rawQuery(
                    "SELECT r_group_code FROM rasp_update WHERE r_group_code = '" + MainActivity.selectedItem_id + "'",
                    null
                )
                val checkBox = view?.findViewById<CheckBox>(R.id.checkBox)
                checkBox?.isChecked = sss.count != 0
                checkBox?.setOnClickListener {
                    if (checkBox.isChecked) {
                        checkBox.setTextColor(Color.GREEN)
                        val rowValues = ContentValues() // Значения для вставки в базу данных
                        rowValues.put("r_group_code", MainActivity.selectedItem_id)
                        rowValues.put("r_selectedItem_type", MainActivity.selectedItem_type)
                        rowValues.put("r_selectedItem", MainActivity.selectedItem)
                        sqLiteDatabase.insert("rasp_update", null, rowValues)
                    } else {
                        checkBox.setTextColor(Color.GRAY)
                        sqLiteDatabase.delete(
                            "rasp_update",
                            "r_group_code = '" + MainActivity.selectedItem_id + "'",
                            null
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}