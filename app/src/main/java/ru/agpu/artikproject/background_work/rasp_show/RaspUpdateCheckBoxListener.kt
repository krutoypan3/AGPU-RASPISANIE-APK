package ru.agpu.artikproject.background_work.rasp_show

import android.content.ContentValues
import android.graphics.Color
import android.view.View
import android.widget.CheckBox
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.DataBaseSqlite
import ru.agpu.artikproject.background_work.datebase.RaspUpdate
import ru.agpu.artikproject.background_work.datebase.RaspUpdateRepository
import ru.agpu.artikproject.presentation.layout.MainActivity

class RaspUpdateCheckBoxListener(view: View?) {
    init {
        try {
            val raspUpdateRepository = RaspUpdateRepository()
            val checkBox = view?.findViewById<CheckBox>(R.id.checkBox)
            checkBox?.isChecked = raspUpdateRepository.getByParams(RaspUpdate(
                groupCode = MainActivity.selectedItem_id.toIntOrNull()
            )).isNotEmpty()
            checkBox?.setOnClickListener {
                val raspUpdate = RaspUpdate(
                    groupCode = MainActivity.selectedItem_id.toIntOrNull(),
                    searchType = MainActivity.selectedItem_type,
                    paraName = MainActivity.selectedItem,
                )

                if (checkBox.isChecked) {
                    checkBox.setTextColor(Color.GREEN)
                    raspUpdateRepository.saveRaspUpdate(raspUpdate)
                } else {
                    checkBox.setTextColor(Color.GRAY)
                    raspUpdateRepository.deleteRaspUpdate(raspUpdate)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}