package ru.agpu.artikproject.background_work.rasp_show

import android.graphics.Color
import android.view.View
import android.widget.CheckBox
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItem
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItemId
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItemType
import ru.agpu.artikproject.background_work.datebase.RaspUpdate
import ru.agpu.artikproject.background_work.datebase.RaspUpdateRepository

class RaspUpdateCheckBoxListener(view: View?) {
    init {
        try {
            val raspUpdateRepository = RaspUpdateRepository()
            val checkBox = view?.findViewById<CheckBox>(R.id.checkBox)
            checkBox?.setOnCheckedChangeListener { _, isChecked ->
                val raspUpdate = RaspUpdate(
                    groupCode = selectedItemId?.toIntOrNull(),
                    searchType = selectedItemType,
                    paraName = selectedItem,
                )

                if (isChecked) {
                    checkBox.setTextColor(Color.GREEN)
                    raspUpdateRepository.saveRaspUpdate(raspUpdate)
                } else {
                    checkBox.setTextColor(Color.GRAY)
                    raspUpdateRepository.deleteRaspUpdate(raspUpdate)
                }
            }
            checkBox?.isChecked = raspUpdateRepository.getByParams(RaspUpdate(
                groupCode = selectedItemId?.toIntOrNull()
            )).isNotEmpty()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}