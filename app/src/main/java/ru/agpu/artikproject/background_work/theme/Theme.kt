package ru.agpu.artikproject.background_work.theme

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences

object Theme {
    var current_theme = 0

    /**
     * Применяет сохраненную тему к приложению
     * @param act Активити
     */
    fun setting(act: Activity) {
        current_theme = get(act.applicationContext)
        AppCompatDelegate.setDefaultNightMode(current_theme)
    }

    /**
     * Получить изменить тему приложения
     * @param context Контекст приложения
     * @param new_theme Id новой темы
     */
    operator fun set(context: Context?, new_theme: Int) {
        MySharedPreferences.putPref(context, "currentTheme", new_theme)
    }

    /**
     * Получить сохраненную тему приложения
     * @param context Контекст приложения
     * @return Id сохраненной темы
     */
    operator fun get(context: Context?): Int {
        return MySharedPreferences.getPref(context, "currentTheme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    /**
     * Получает тему приложения.
     * Если в приложении установлена тема системы, то получает системную тему.
     * @param context Контекст приложения
     * @return Id сохраненной темы
     */
    fun getApplicationTheme(context: Context): Int {
        var theme = MySharedPreferences.getPref(context, "currentTheme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        if (theme == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            theme = getCurrentSystemTheme(context)
        return theme
    }

    /**
     * Получить текущую системную тему (светлая или темная). В неопределенном случае возвращает системную
     * @param context Контекст приложения
     * @return Id сохраненной темы
     */
    fun getCurrentSystemTheme(context: Context): Int {
        return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_NO
            Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }
}