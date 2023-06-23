package ru.agpu.artikproject.background_work.settings_layout.ficha

import android.annotation.SuppressLint
import android.app.Activity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements.Companion.put
import java.util.Random

/**
 * Слкшатель нажатий на фон в настройках (пасхалочки)
 * @param act Активити
 */
@SuppressLint("ClickableViewAccessibility")
class BackgroundDarkerListener(act: Activity) {
    init {
        val updateLayout = act.findViewById<RelativeLayout>(R.id.update_layout) // Находим сам фон
        val animImage = act.findViewById<ImageView>(R.id.animImage) // Находим изначально невидимое пустое изображение
        updateLayout.setOnTouchListener { _: View?, event: MotionEvent ->  // При нажатии на фон
            animImage.x = event.x - 100 // Определяем позицию нажатия по X
            animImage.y = event.y - 100 // Определяем позицию нажатия по Y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    put(act.applicationContext, "ficha_setting_logo") // Засчитываем фичу с плавающим логотипом
                    FichaShow(act) // Обновляем список найденных фич, т.к. находимся на слое настроек
                    animImage.visibility = View.VISIBLE // Делаем невидимую картинку видимой
                    if (Random().nextInt(20) == 0) { // С шансом 1 к 20 устанавливаем другое фото вместо логотипа
                        animImage.setImageResource(R.drawable.ficha_leonardo) // Ставим фото Леонардо
                        put(act.applicationContext, "ficha_setting_leonardo") // Засчитываем фичу
                    }
                }
                MotionEvent.ACTION_UP -> {
                    animImage.visibility = View.INVISIBLE // Скрываем плавающий логотип
                    animImage.setImageResource(R.drawable.agpu_ico) // И возвращаем его (если была активирована фича с Леонардо)
                }
            }
            true
        }
    }
}