package ru.agpu.artikproject.background_work.settings_layout.user_background

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.SeekBar
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences

/**
 * Устанавливает ползунки в сохраненное состояние
 * @param act Активити
 * @param seekBar Ползунок
 * @param lightSeekBar Светлый ползунок(true) или темный(false)
 */
class UserSeekBarSetting(act: Activity, seekBar: SeekBar, lightSeekBar: Boolean) {
    init {
        if (lightSeekBar) { // Если ползунок светлый
            val level = MySharedPreferences[act.applicationContext, "light_darker_level", 30] // Получаем сохраненный уровень ползунка (если его нет, то ставим 30%)
            seekBar.progress = level // Устанавливаем полученый уровень
            act.findViewById<View>(R.id.background_light_image_selector_darker).setBackgroundColor(
                Color.argb((level * 2.5).toInt(), 255, 255, 255)
            ) // Затемняем мини-фон
        } else { // Если ползунок темный
            val level = MySharedPreferences[act.applicationContext, "dark_darker_level", 30] // Получаем сохраненный уровень ползунка (если его нет, то ставим 30%)
            seekBar.progress = level // Устанавливаем полученый уровень
            act.findViewById<View>(R.id.background_dark_image_selector_darker).setBackgroundColor(
                Color.argb((level * 2.5).toInt(), 0, 0, 0)
            ) // Затемняем мини-фон
        }
    }
}