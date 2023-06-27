package ru.agpu.artikproject.background_work.theme

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences
import java.io.File
import java.time.LocalDate
import java.time.Month

object CustomBackground {
    /**
     * Функция получения фона в зависимости от текущей темы
     * @param context Контекст приложения
     * @return Drawable - изображение
     */
    fun getBackground(context: Context): Drawable? {
        val theme = Theme.getApplicationTheme(context) // Получаем тему приложения
        if (MySharedPreferences[context, "enable_background_user", false]) { // Если включен пользовательский фон
            return if (theme == AppCompatDelegate.MODE_NIGHT_NO)
                getBackgroundDrawable(context, "background_light") // Если светлая, то возвращаем светлый фон
            else
                getBackgroundDrawable(context, "background_dark") // Если темная, то возвращаем темный фон
        }
        // Проверяем ивентовые фоны
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date = LocalDate.now()
            // 14 Февраля - день влюблённых
            if (date.month == Month.FEBRUARY && date.dayOfMonth == 14) {
                return if (theme == AppCompatDelegate.MODE_NIGHT_NO) {
                    AppCompatResources.getDrawable(context, R.drawable.event_background_pic_14_february_light)
                } else
                    AppCompatResources.getDrawable(context, R.drawable.event_background_pic_14_february_dark)
            }
        } else { // Игнорим ивенты
            // Серьезно? Чел, купи себе новый телефон, Сейчас еще существуют люди использующие телефоны с андроидом ниже 8?
        }


        // Если пользовательский фон отключен, то возвращаем стандартный фон
        return if (theme == AppCompatDelegate.MODE_NIGHT_NO)
            AppCompatResources.getDrawable(context, R.drawable.background_light)
        else
            AppCompatResources.getDrawable(context, R.drawable.background_dark)
    }

    /**
     * Функция получения затемнителя фона в зависимости от текущей темы
     * @param context Контекст приложения
     * @return Color - цвет картинки
     */
    fun getBackgroundDarker(context: Context): Int {
        return if (Theme.getApplicationTheme(context) == AppCompatDelegate.MODE_NIGHT_YES) { // Получаем тему приложения в настройках
            getBackgroundDarkerColor(context, false) // Если темная -  затемнитель темный
        } else
            getBackgroundDarkerColor(context, true) // Если тема светлая то ставим стандартный фон
    }

    // Микрофункция возвращающая картинку в зависимости от темы
    private fun getBackgroundDrawable(context: Context, background_type: String): Drawable? {
        val file = File(MySharedPreferences[context, background_type, ""]) // Получаем картинку фона
        if (file != File("")) { // Если путь к файлу не пустой, то возвращаем картинку
            return BitmapDrawable(context.resources, BitmapFactory.decodeFile(file.absolutePath))
        } // Если файл пустой - возвращаем стандартные обои
        return if (Theme.getApplicationTheme(context) == AppCompatDelegate.MODE_NIGHT_NO)
            AppCompatResources.getDrawable(context, R.drawable.background_light)
        else
            AppCompatResources.getDrawable(context, R.drawable.background_dark)
    }

    // Микрофункция возвращающая цвет в зависимости от темы
    private fun getBackgroundDarkerColor(context: Context, lightDarker: Boolean): Int {
        var level = 30
        if (lightDarker) { // Если тема светлая, то возвращаем светлый затемнитель
            if (MySharedPreferences[context, "enable_background_user", false])
                level = MySharedPreferences[context, "light_darker_level", 30]
            return Color.argb((level * 2.5).toInt(), 255, 255, 255)
        } // Если тема темная, то возвращаем темный затемнитель
        if (MySharedPreferences[context, "enable_background_user", false])
            level = MySharedPreferences[context, "dark_darker_level", 30]
        return Color.argb((level * 2.5).toInt(), 0, 0, 0)
    }
}