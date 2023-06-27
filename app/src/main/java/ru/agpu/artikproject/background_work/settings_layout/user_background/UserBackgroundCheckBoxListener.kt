package ru.agpu.artikproject.background_work.settings_layout.user_background

import android.app.Activity
import android.widget.CheckBox
import android.widget.CompoundButton
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences

/**
 * Слушатель нажатий на userBackgroundCheckBox
 *
 * @param act                    Активити
 * @param userBackgroundCheckBox Чекбокс
 */
class UserBackgroundCheckBoxListener(act: Activity, userBackgroundCheckBox: CheckBox) {
    init {
        userBackgroundCheckBox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            MySharedPreferences.put(act.applicationContext, "enable_background_user", isChecked)
            UserBackgroundCheckboxSetVisible(act, isChecked)
        }
    }
}