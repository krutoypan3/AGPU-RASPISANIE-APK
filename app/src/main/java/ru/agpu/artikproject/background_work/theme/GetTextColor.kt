package ru.agpu.artikproject.background_work.theme

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import ru.agpu.artikproject.R

object GetTextColor {
    /**
     * Возвращает цвет текста для текущей темы приложения
     * @param context Контекст приложения
     * @return R.color.(цвет)
     */
    fun getAppColor(context: Context): Int {
        val theme = Theme.getApplicationTheme(context)
        return if (theme == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            getSystemColor(context) // Если тема в приложении стоит как в системе (По-хорошему этот пункт срабатывать не должен)
        } else { // Если тема в приложении отличается от системной
            if (theme == AppCompatDelegate.MODE_NIGHT_YES) {
                context.getColor(R.color.gold) // Включена темная тема
            } else {
                context.getColor(R.color.black) // Включена светлая тема
            }
        }
    }

    /**
     * Возвращает обратный цвет текста для текущей темы приложения
     * @param context Контекст приложения
     * @return R.color.(цвет)
     */
    fun getAppColorInvert(context: Context): Int {
        val color = getAppColor(context)
        return if (color == context.getColor(R.color.black))
            context.getColor(R.color.gold)
        else
            context.getColor(R.color.black)
    }

    /**
     * Возвращает цвет текста для текущей темы системы
     * @param context Контекст приложения
     * @return R.color.(цвет)
     */
    private fun getSystemColor(context: Context): Int {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) // Если текущая тема системы
            context.getColor(R.color.black) // светлая
        else
            context.getColor(R.color.gold) // темная
    }
}