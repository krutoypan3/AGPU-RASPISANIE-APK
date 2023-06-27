package ru.agpu.artikproject.background_work.settings_layout.user_background

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatDelegate
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences
import ru.agpu.artikproject.background_work.theme.Theme

/**
 * Слушатель изменения ползунка затемнителя
 * @param act Активити
 * @param seekBar SeekBar (ползунок)
 * @param isLightSeekBar Светлый ползунок (true) или темный (false)
 */
class UserSeekBarListener(act: Activity, seekBar: SeekBar, isLightSeekBar: Boolean) {
    init {
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val color: Int
                if (isLightSeekBar) { // Если светлый ползунок
                    color = Color.argb((seekBar.progress * 2.5).toInt(), 255, 255, 255) // Делаем затемнитель
                    act.findViewById<View>(R.id.background_light_image_selector_darker)
                        .setBackgroundColor(color) // Затемняем мини-фон
                } else { // Если темный ползунок
                    color = Color.argb((seekBar.progress * 2.5).toInt(), 0, 0, 0) // Делаем засветлитель
                    act.findViewById<View>(R.id.background_dark_image_selector_darker)
                        .setBackgroundColor(color) // Затемняем мини-фон
                }
                var theme = Theme.get(act.applicationContext)
                if (theme == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
                    theme = Theme.getCurrentSystemTheme(act.applicationContext)
                }
                // Если текущая тема совпадает с темой изменяемого ползунка
                if (theme == AppCompatDelegate.MODE_NIGHT_NO == isLightSeekBar)
                    act.findViewById<View>(R.id.background_darker).setBackgroundColor(color) // Меняем цвет фона в настройках
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // После того как пользователь отпустит ползунок, сохраняем новые значения затемнителя
                if (isLightSeekBar) // Если светлый ползунок - сохраняем засветлитель
                    MySharedPreferences.put(act.applicationContext, "light_darker_level", seekBar.progress)
                else  // Если темный ползунок - сохраняем затемнитель
                    MySharedPreferences.put(act.applicationContext, "dark_darker_level", seekBar.progress)
            }
        })
    }
}