package ru.agpu.artikproject.background_work.settings_layout.user_background

import android.app.Activity
import android.widget.CheckBox
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences

/**
 * Настраивает ЧекБокс, отвечающий за смену пользовательского фона
 * @param act Активити
 * @param userBackgroundCheckBox ЧекБокс
 */
class UserBackgroundCheckBoxSetting(act: Activity, userBackgroundCheckBox: CheckBox) {
    init {
        if (MySharedPreferences[act.applicationContext, "enable_background_user", false]) {
            userBackgroundCheckBox.isChecked = true
            UserBackgroundCheckboxSetVisible(act, true)
        }
    }
}