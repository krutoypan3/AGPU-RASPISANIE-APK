package ru.agpu.artikproject.background_work.settings_layout.user_background

import android.app.Activity
import android.view.View
import android.widget.GridLayout
import ru.agpu.artikproject.R

/**
 * Данный класс изменяет видимость слоя с настройкой фона и затемнителя
 * @param act Активити
 * @param is_visible Сделать видимым(true) \ невидимым(false)
 */
class UserBackgroundCheckboxSetVisible(act: Activity, is_visible: Boolean) {
    init {
        val layout = act.findViewById<GridLayout>(R.id.layout_change_background) // Слой с элементами настройки фона

        if (is_visible) { // Если включаем видимость
            layout.visibility = View.VISIBLE // Делаем фон видимым
            layout.layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT // Растягиваем по размеру элементов
            layout.layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT // Растягиваем по размеру элементов
        } else { // Если выключаем видимость
            layout.visibility = View.INVISIBLE // Делаем фон невидимым
            layout.layoutParams.width = 0 // Сужаем слой до 0
            layout.layoutParams.height = 0 // Сужаем слой до 0
        }
        layout.requestLayout() // Обновляем элемент на слое
    }
}