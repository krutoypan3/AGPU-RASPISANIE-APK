package ru.agpu.artikproject.background_work.settings_layout.user_background

import android.app.Activity
import android.content.Intent
import android.widget.ImageView
import ru.agpu.artikproject.background_work.image_utils.ImageSelector

/**
 * Прослушивает нажатия на картинку с выбором фонового изображения
 * @param act Активити
 * @param userBackgroundLightSelector Изменяемый ImageView
 * @param darkOrLight Светлый фон(background_light) или темный(background_dark)
 */
class UserBackgroundSelectorListener(act: Activity, userBackgroundLightSelector: ImageView, darkOrLight: String) {
    init {
        userBackgroundLightSelector.setOnClickListener {
            val imageSelector = Intent(
                act.applicationContext,
                ImageSelector::class.java
            )
            imageSelector.putExtra("background", darkOrLight)
            act.startActivityForResult(imageSelector, 1)
        }
    }
}