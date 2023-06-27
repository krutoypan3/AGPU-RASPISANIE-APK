package ru.agpu.artikproject.background_work.settings_layout.theme_radio_group

import android.app.Activity
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatDelegate
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.theme.Theme

/**
 * Установка активного RadioButton
 * @param act Активити
 */
class ThemeRadioButtonSetting(act: Activity) {
    init {
        val radiobuttonSystem = act.findViewById<RadioButton>(R.id.theme_system)
        val radiobuttonDark = act.findViewById<RadioButton>(R.id.theme_dark)
        val radiobuttonLight = act.findViewById<RadioButton>(R.id.theme_light)
        when (Theme.get(act.applicationContext)) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> radiobuttonSystem.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> radiobuttonDark.isChecked = true
            AppCompatDelegate.MODE_NIGHT_NO -> radiobuttonLight.isChecked = true
        }
    }
}