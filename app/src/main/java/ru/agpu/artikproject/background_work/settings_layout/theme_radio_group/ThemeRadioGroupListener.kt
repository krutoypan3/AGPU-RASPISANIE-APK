package ru.agpu.artikproject.background_work.settings_layout.theme_radio_group

import android.app.Activity
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.theme.Theme

/**
 * Прослушивает нажатия на элементы ThemeRadioGroup
 * @param act Активити
 */
class ThemeRadioGroupListener(act: Activity) {
    init {
        val themeRadioGroup = act.findViewById<RadioGroup>(R.id.theme_radio_group)
        themeRadioGroup.setOnCheckedChangeListener { _: RadioGroup?, checkedId: Int ->
            when (checkedId) {
                R.id.theme_system -> Theme.setTheme(
                    act.applicationContext,
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )

                R.id.theme_dark -> Theme.setTheme(
                    act.applicationContext,
                    AppCompatDelegate.MODE_NIGHT_YES
                )

                R.id.theme_light -> Theme.setTheme(
                    act.applicationContext,
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            }
            Toast.makeText(act.applicationContext, R.string.theme_apply, Toast.LENGTH_SHORT).show()
        }
    }
}