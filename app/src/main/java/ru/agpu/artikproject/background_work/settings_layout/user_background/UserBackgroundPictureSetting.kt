package ru.agpu.artikproject.background_work.settings_layout.user_background

import android.app.Activity
import android.net.Uri
import android.widget.ImageView
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences

/**
 * Устанавливает картинку на задний фон в настройках фона
 * @param act Активити
 * @param userBackgroundLightSelector Светлый фон
 * @param userBackgroundDarkSelector Темный фон
 */
class UserBackgroundPictureSetting(
    act: Activity,
    userBackgroundLightSelector: ImageView,
    userBackgroundDarkSelector: ImageView
) {
    init {
        val backgroundLight = MySharedPreferences[act.applicationContext, "background_light", ""]
        if (backgroundLight != "") { // Если светлая картинка есть
            userBackgroundLightSelector.setImageURI(null) // Без обнуления новая картинка не встанет
            userBackgroundLightSelector.setImageURI(Uri.parse(backgroundLight)) // Установка новой картинки
        }
        val backgroundDark = MySharedPreferences[act.applicationContext, "background_dark", ""]
        if (backgroundDark != "") { // Если темная картинка есть
            userBackgroundDarkSelector.setImageURI(null) // Без обнуления новая картинка не встанет
            userBackgroundDarkSelector.setImageURI(Uri.parse(backgroundDark)) // Установка новой картинки
        }
    }
}