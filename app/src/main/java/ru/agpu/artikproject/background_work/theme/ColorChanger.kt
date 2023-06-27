package ru.agpu.artikproject.background_work.theme

import android.graphics.Color

/**
 * Класс, позволяющий проводить некоторые манипуляции с цветами
 */
object ColorChanger {
    /**
     * Позволяет получить цвет темнее того, что подается на вход
     * @param color Исходный цвет
     * @param level Уровень затемнения [0..255]
     * @return Затемнённый цвет
     */
    fun getDarkColor(color: Int, level: Int): Int {
        if (-255 > level || level > 255) // Проверяем входной уровень затемнения\засветления
            return color // Если не входит в рамки, возвращаем исхождный цвет
        var red = Color.red(color) - level
        if (red < 0) red = 0
        if (red > 255) red = 255
        var green = Color.green(color) - level
        if (green < 0) green = 0
        if (green > 255) green = 255
        var blue = Color.blue(color) - level
        if (blue < 0) blue = 0
        if (blue > 255) blue = 255
        return Color.rgb(red, green, blue)
    }

    /**
     * Позволяет получить цвет светлее того, что подается на вход
     * @param color Исходный цвет
     * @param level Уровень засветления [0..255]
     * @return Засветлённый цвет
     */
    fun getLightColor(color: Int, level: Int) = getDarkColor(color, -level)
}